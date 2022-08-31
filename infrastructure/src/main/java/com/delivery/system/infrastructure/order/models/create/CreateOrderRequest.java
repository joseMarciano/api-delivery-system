package com.delivery.system.infrastructure.order.models.create;

public record CreateOrderRequest(
        String name,
        String driverId
) {
}
