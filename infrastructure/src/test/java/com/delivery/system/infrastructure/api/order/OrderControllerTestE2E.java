package com.delivery.system.infrastructure.api.order;

import com.delivery.system.E2ETest;
import com.delivery.system.configs.json.Json;
import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.order.Order;
import com.delivery.system.domain.order.OrderID;
import com.delivery.system.domain.order.OrderNotifier;
import com.delivery.system.infrastructure.driver.persistence.DriverJpaEntity;
import com.delivery.system.infrastructure.driver.persistence.DriverRepository;
import com.delivery.system.infrastructure.order.models.create.CreateOrderRequest;
import com.delivery.system.infrastructure.order.persistence.OrderJpaEntity;
import com.delivery.system.infrastructure.order.persistence.OrderRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@E2ETest
@Testcontainers
public class OrderControllerTestE2E {

    private static final String BASE_PATH = "/orders";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private OrderRepository orderRepository;

    @SpyBean
    private OrderController orderController;

    @SpyBean
    private OrderNotifier orderNotifier;

    @SpyBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Container
    public static PostgreSQLContainer<?> postgresDB =
            new PostgreSQLContainer<>("postgres:14.5")
                    .withDatabaseName("order_manager")
                    .withUsername("order_owner")
                    .withPassword("a1b2c3f4e5");

    @Container
    public static RabbitMQContainer rabbitContainer =
            new RabbitMQContainer("rabbitmq:3.10.7");


    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username", postgresDB::getUsername);
        registry.add("spring.datasource.password", postgresDB::getPassword);
        registry.add("spring.datasource.port", () -> postgresDB.getMappedPort(5432));

        registry.add("spring.rabbitmq.host", rabbitContainer::getHost);
        registry.add("spring.rabbitmq.username", rabbitContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitContainer::getAdminPassword);
        registry.add("spring.rabbitmq.port", () -> rabbitContainer.getMappedPort(5672));
    }


    @Test
    public void asAUser_IShouldBeAbleToCreateAOrderWithValidValues() throws Exception {
        assertEquals(0, rabbitAdmin.getQueueInfo("order.created").getMessageCount());
        assertEquals(0, driverRepository.count());
        assertEquals(0, orderRepository.count());

        final var aDriver = Driver.newDriver("John");
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        assertEquals(1, driverRepository.count());

        final var expectedDriverId = aDriver.getId();
        final var expectedDescription = "Package 1";
        final var aRequestInput = new CreateOrderRequest(expectedDescription, expectedDriverId.getValue());


        final var request =
                MockMvcRequestBuilders.post(BASE_PATH)
                        .content(Json.writeValueAsString(aRequestInput))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);


        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated());

        assertEquals(1, orderRepository.count());
        final var actualEntity = orderRepository.findAll().get(0);

        Assertions.assertNotNull(actualEntity.getId());
        assertEquals(actualEntity.getDescription(), expectedDescription);
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNull(actualEntity.getDeliveredAt());
        assertEquals(actualEntity.getCreatedAt(), actualEntity.getUpdatedAt());
        assertEquals(actualEntity.getDriver().getId(), expectedDriverId.getValue());

        verify(orderNotifier).notifyCreated(OrderID.from(actualEntity.getId()));
        verify(rabbitTemplate).convertAndSend("order.created", Json.writeValueAsString(OrderID.from(actualEntity.getId())));
        assertEquals(1, rabbitAdmin.getQueueInfo("order.created").getMessageCount());
    }

    @Test
    public void asAUser_IShouldBeAbleToSeeTreatedErrorOnCreateOrderWithEmptyName() throws Exception {
        assertEquals(0, rabbitAdmin.getQueueInfo("order.created").getMessageCount());
        assertEquals(0, driverRepository.count());
        assertEquals(0, orderRepository.count());

        final var aDriver = Driver.newDriver("John");
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        assertEquals(1, driverRepository.count());

        final var expectedDescription = "  ";
        final var expectedErrorMessage = "'description' should not be null";
        final var expectedErrorCount = 1;

        final var aRequestInput = new CreateOrderRequest(expectedDescription, aDriver.getId().getValue());

        final var request =
                MockMvcRequestBuilders.post(BASE_PATH)
                        .content(Json.writeValueAsString(aRequestInput))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);


        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        Assertions.assertEquals(0, orderRepository.count());


        verify(orderNotifier, times(0)).notifyCreated(any());
        verify(rabbitTemplate, times(0)).convertAndSend(anyString(), any(String.class));
        assertEquals(0, rabbitAdmin.getQueueInfo("order.created").getMessageCount());
    }

    @Test
    public void asAUser_IShouldBeAbleToFindAllExistentOrdersSuccessfully() throws Exception {
        assertEquals(0, driverRepository.count());
        assertEquals(0, orderRepository.count());

        final var aDriver = Driver.newDriver("John");
        final var expectedDriverId = aDriver.getId();

        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        assertEquals(1, driverRepository.count());

        final var orders = List.of(
                Order.newOrder("Pack 1", expectedDriverId),
                Order.newOrder("Pack 2", expectedDriverId),
                Order.newOrder("Pack 3", expectedDriverId)
        );

        final var expectedOrdersCount = 3;

        Assertions.assertEquals(0, orderRepository.count());
        orderRepository.saveAllAndFlush(mapTo(orders, OrderJpaEntity::from));
        Assertions.assertEquals(expectedOrdersCount, orderRepository.count());


        final var request =
                MockMvcRequestBuilders.get(BASE_PATH)
                        .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedOrdersCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.equalTo(orders.get(0).getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.equalTo(orders.get(0).getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].deliveredAt", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].statusOrder", Matchers.equalTo("CREATED")))

                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.equalTo(orders.get(1).getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description", Matchers.equalTo(orders.get(1).getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].deliveredAt", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].statusOrder", Matchers.equalTo("CREATED")))


                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id", Matchers.equalTo(orders.get(2).getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].description", Matchers.equalTo(orders.get(2).getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].deliveredAt", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].statusOrder", Matchers.equalTo("CREATED")))
                .andReturn();
    }


    @Test
    public void asAUser_IShouldBeAbleToSeeAEmptyListWhenOrdersNotExists() throws Exception {
        final var expectedOrdersCount = 0;
        Assertions.assertEquals(0, orderRepository.count());

        final var request =
                MockMvcRequestBuilders.get(BASE_PATH)
                        .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedOrdersCount)))
                .andReturn();
    }

    @Test
    public void asAUser_IShouldBeAbleToSeeATreatedInternalServerErrorOnRandomException() throws Exception {
        final var expectedMessage = "Internal server error";
        final var expectedErrorMessage = "Controller error";

        Mockito.when(orderController.listAll()).thenThrow(new RuntimeException(expectedErrorMessage));

        final var request =
                MockMvcRequestBuilders.get(BASE_PATH)
                        .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)))
                .andReturn();
    }

    private <I, O> List<O> mapTo(List<I> list, Function<I, O> mapper) {
        return list.stream().map(mapper).toList();
    }


}
