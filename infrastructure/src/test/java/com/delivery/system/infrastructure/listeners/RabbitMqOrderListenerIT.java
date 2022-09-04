package com.delivery.system.infrastructure.listeners;

import com.delivery.system.IntegrationTest;
import com.delivery.system.RabbitMQListenersCleanUpExtension;
import com.delivery.system.configs.json.Json;
import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.order.Order;
import com.delivery.system.infrastructure.driver.persistence.DriverJpaEntity;
import com.delivery.system.infrastructure.driver.persistence.DriverRepository;
import com.delivery.system.infrastructure.order.persistence.OrderJpaEntity;
import com.delivery.system.infrastructure.order.persistence.OrderRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.delivery.system.domain.order.StatusOrder.*;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@Testcontainers
@ExtendWith(RabbitMQListenersCleanUpExtension.class) //TODO: PUT THIS IN INTEGRATION TESTS ANOTATION
public class RabbitMqOrderListenerIT {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RabbitListenerEndpointRegistry listenerRegistry;

    @Container
    public static RabbitMQContainer rabbitContainer =
            new RabbitMQContainer("rabbitmq:3.10.7");


    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitContainer::getHost);
        registry.add("spring.rabbitmq.username", rabbitContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitContainer::getAdminPassword);
        registry.add("spring.rabbitmq.port", () -> rabbitContainer.getMappedPort(5672));
    }

    @Test
    public void givenAValidOrder_whenMessagingIsOrderDelivered_shouldUpdateOrder() {
        assertEquals(0, rabbitAdmin.getQueueInfo("order.delivered").getMessageCount());
        assertEquals(0, driverRepository.count());
        assertEquals(0, orderRepository.count());

        listenerRegistry.getListenerContainer("order.delivered").start();

        final var expectedStatusOrder = DELIVERED;

        final var aOrder = createOrder();
        assertEquals(1, driverRepository.count());
        assertEquals(1, orderRepository.count());


        assertEquals(CREATED, aOrder.getStatus());
        assertNull(aOrder.getTimeDelivered());

        rabbitTemplate.convertAndSend("order.delivered", Json.writeValueAsString(aOrder.getId()));

        Awaitility.await().atMost(30, TimeUnit.SECONDS)
                .until(verifyIfQueueIsEmpty("order.delivered"), Boolean.TRUE::equals);

        final var orderEntity = orderRepository.findAll().get(0);
        assertEquals(expectedStatusOrder, orderEntity.getStatus());
        assertNotNull(orderEntity.getDeliveredAt());
    }

    @Test
    public void givenAValidOrder_whenMessagingIsOrderInProgress_shouldUpdateOrder() {
        assertEquals(0, rabbitAdmin.getQueueInfo("order.inProgress").getMessageCount());
        assertEquals(0, driverRepository.count());
        assertEquals(0, orderRepository.count());

        listenerRegistry.getListenerContainer("order.inProgress").start();

        final var expectedStatusOrder = IN_PROGRESS;

        final var aOrder = createOrder();
        assertEquals(1, driverRepository.count());
        assertEquals(1, orderRepository.count());


        assertEquals(CREATED, aOrder.getStatus());
        assertNull(aOrder.getTimeDelivered());

        rabbitTemplate.convertAndSend("order.inProgress", Json.writeValueAsString(aOrder.getId()));

        Awaitility.await().atMost(30, TimeUnit.SECONDS)
                .until(verifyIfQueueIsEmpty("order.inProgress"), Boolean.TRUE::equals);

        final var orderEntity = orderRepository.findAll().get(0);
        assertEquals(expectedStatusOrder, orderEntity.getStatus());
        assertNull(orderEntity.getDeliveredAt());
    }

    public Callable<Boolean> verifyIfQueueIsEmpty(final String queueName) {
        return () -> rabbitAdmin.getQueueInfo(queueName).getMessageCount() == 0;
    }

    public Order createOrder() {
        final var aDriver = Driver.newDriver("John");
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        final var aOrder = Order.newOrder("Package 1", aDriver.getId());
        return orderRepository.saveAndFlush(OrderJpaEntity.from(aOrder)).toAggregate();
    }

}
