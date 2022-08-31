package com.delivery.system.infrastructure.order.models.create;

public record CreateOrderRequest(
        String description,
        String driverId
) {
}
