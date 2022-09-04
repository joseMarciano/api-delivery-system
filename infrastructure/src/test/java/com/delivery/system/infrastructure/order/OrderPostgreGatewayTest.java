package com.delivery.system.infrastructure.order;

import com.delivery.system.PostgreGatewayTest;
import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.order.Order;
import com.delivery.system.domain.order.OrderID;
import com.delivery.system.domain.order.StatusOrder;
import com.delivery.system.infrastructure.driver.persistence.DriverJpaEntity;
import com.delivery.system.infrastructure.driver.persistence.DriverRepository;
import com.delivery.system.infrastructure.order.persistence.OrderJpaEntity;
import com.delivery.system.infrastructure.order.persistence.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@PostgreGatewayTest
public class OrderPostgreGatewayTest {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderPostgreGateway orderPostgreGateway;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void givenAValidOrder_whenCallsCreateOrder_shouldReturnOrderCreated() {
        final var aDriver = Driver.newDriver("John");
        final var aOrder = Order.newOrder("Package 1", aDriver.getId());

        assertEquals(0, driverRepository.count());
        assertEquals(0, orderRepository.count());
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        assertEquals(1, driverRepository.count());

        final var actualOrder = orderPostgreGateway.create(aOrder);

        assertEquals(aOrder.getId(), actualOrder.getId());
        assertEquals(aOrder.getDescription(), actualOrder.getDescription());
        assertEquals(aOrder.getDriverID(), actualOrder.getDriverID());
        Assertions.assertNotNull(aOrder.getCreatedAt());
        Assertions.assertNotNull(aOrder.getUpdatedAt());
        Assertions.assertNull(aOrder.getTimeDelivered());
        assertEquals(aOrder.getCreatedAt(), actualOrder.getCreatedAt());
        assertEquals(aOrder.getUpdatedAt(), actualOrder.getUpdatedAt());

        final var actualEntity =
                orderRepository.findById(actualOrder.getId().getValue()).get();

        assertEquals(actualEntity.getId(), actualOrder.getId().getValue());
        assertEquals(actualEntity.getDescription(), actualOrder.getDescription());
        assertEquals(actualEntity.getStatus(), StatusOrder.CREATED);
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeliveredAt());
        assertEquals(actualEntity.getCreatedAt(), actualOrder.getCreatedAt());
        assertEquals(actualEntity.getUpdatedAt(), actualOrder.getUpdatedAt());
    }

    @Test
    public void givenAValidPrePersistedOrder_whenCallsUpdateOrder_shouldReturnOrderUpdated() {
        final var aDriver = Driver.newDriver("John");
        final var aOrder = Order.newOrder("Pacge 1", aDriver.getId());

        final var expectedId = aOrder.getId();
        final var expectedDescription = "Package 1";
        final var expectedDriverId = aDriver.getId();

        assertEquals(0, driverRepository.count());
        assertEquals(0, orderRepository.count());
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        assertEquals(1, driverRepository.count());
        orderRepository.save(OrderJpaEntity.from(aOrder));
        assertEquals(1, orderRepository.count());

        final var aOrderUpdated = aOrder.clone().update(expectedDescription, expectedDriverId);

        final var actualOrder = orderPostgreGateway
                .update(aOrderUpdated);

        assertEquals(actualOrder.getId(), expectedId);
        assertEquals(actualOrder.getDescription(), expectedDescription);
        assertEquals(actualOrder.getStatus(), StatusOrder.CREATED);
        assertNull(actualOrder.getTimeDelivered());
        assertNotNull(actualOrder.getCreatedAt());
        assertNotNull(actualOrder.getUpdatedAt());
        assertTrue(actualOrder.getUpdatedAt().isAfter(aOrder.getCreatedAt()));
    }

    @Test
    public void givenAValidPrePersistedOrder_whenCallsFindById_shouldReturnAOrder() {

        final var expectedDescription = "Package 1";

        final var aDriver = Driver.newDriver("John");
        final var aOrder = Order.newOrder(expectedDescription, aDriver.getId());

        final var expectedId = aOrder.getId();

        assertEquals(0, driverRepository.count());
        assertEquals(0, orderRepository.count());
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        assertEquals(1, driverRepository.count());
        orderRepository.save(OrderJpaEntity.from(aOrder));
        assertEquals(1, orderRepository.count());

        final var actualOrder = orderPostgreGateway.findById(expectedId).get();

        assertEquals(expectedId, actualOrder.getId());
        assertEquals(expectedDescription, actualOrder.getDescription());
        assertEquals(actualOrder.getStatus(), StatusOrder.CREATED);
        Assertions.assertNotNull(actualOrder.getCreatedAt());
        Assertions.assertNotNull(actualOrder.getUpdatedAt());
        Assertions.assertNull(actualOrder.getTimeDelivered());
        assertEquals(actualOrder.getCreatedAt(), actualOrder.getCreatedAt());
        assertEquals(actualOrder.getUpdatedAt(), actualOrder.getUpdatedAt());
    }

    @Test
    public void givenANoExistingOrder_whenCallsFindById_shouldReturnEmpty() {
        final var expectedId = OrderID.unique();
        Assertions.assertEquals(0, orderRepository.count());
        final var actualOuput = orderPostgreGateway.findById(expectedId);
        Assertions.assertTrue(actualOuput.isEmpty());
    }

    @Test
    public void givenPrePersistedOrders_whenCallsListAllDriver_shouldReturnAListOfOrders() {
        Assertions.assertEquals(0, driverRepository.count());
        final var aDriver = Driver.newDriver("John");
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));


        final var expectedOrders =
                List.of(
                        Order.newOrder("Package 1", aDriver.getId()),
                        Order.newOrder("Package 2", aDriver.getId())
                );

        orderRepository.saveAllAndFlush(mapTo(expectedOrders, OrderJpaEntity::from));
        Assertions.assertEquals(2, orderRepository.count());

        final var actualOrders = orderPostgreGateway.findAll();

        assertTrue(
                actualOrders.size() == expectedOrders.size()
                        && expectedOrders.containsAll(actualOrders)
        );
    }

    @Test
    public void givenNoExistingOrders_whenCallsListAllDriver_shouldReturnAEmptyList() {
        final var expectedSizeList = 0;
        Assertions.assertEquals(0, driverRepository.count());
        final var actualOrders = orderPostgreGateway.findAll();
        assertEquals(expectedSizeList, actualOrders.size());
    }


    private <I, O> List<O> mapTo(List<I> list, Function<I, O> mapper) {
        return list.stream().map(mapper).toList();
    }
}
