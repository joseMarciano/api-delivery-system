package com.delivery.system.infrastructure.api.order;

import com.delivery.system.E2ETest;
import com.delivery.system.configs.json.Json;
import com.delivery.system.domain.driver.Driver;
import com.delivery.system.infrastructure.driver.persistence.DriverJpaEntity;
import com.delivery.system.infrastructure.driver.persistence.DriverRepository;
import com.delivery.system.infrastructure.order.models.create.CreateOrderRequest;
import com.delivery.system.infrastructure.order.persistence.OrderRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

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


    @Container
    public static PostgreSQLContainer<?> postgresDB =
            new PostgreSQLContainer<>("postgres:14.5")
                    .withDatabaseName("order_manager")
                    .withUsername("order_owner")
                    .withPassword("a1b2c3f4e5");


    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username", postgresDB::getUsername);
        registry.add("spring.datasource.password", postgresDB::getPassword);
        registry.add("spring.datasource.port", () -> postgresDB.getMappedPort(5432));
    }


    @Test
    public void asAUser_IShouldBeAbleToCreateAOrderWithValidValues() throws Exception {
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
    }

    @Test
    public void asAUser_IShouldBeAbleToSeeTreatedErrorOnCreateOrderWithEmptyName() throws Exception {
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
    }


}
