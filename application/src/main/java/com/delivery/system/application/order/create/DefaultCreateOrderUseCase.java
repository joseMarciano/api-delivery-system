package com.delivery.system.application.order.create;

import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.domain.order.Order;
import com.delivery.system.domain.order.OrderGateway;

import java.util.Objects;

public class DefaultCreateOrderUseCase extends CreateOrderUseCase {

    private final OrderGateway orderGateway;

    public DefaultCreateOrderUseCase(final OrderGateway orderGateway) {
        this.orderGateway = Objects.requireNonNull(orderGateway, "orderGateway must not be null.");
    }

    @Override
    public CreateOrderOuput execute(CreateOrderCommand anIn) {
        final var aOrder = Order.newOrder(anIn.description(), DriverID.from(anIn.driverId()));
        return CreateOrderOuput.from(orderGateway.create(aOrder));
    }

}
