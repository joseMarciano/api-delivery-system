package com.delivery.system.application.order.create;

public record CreateOrderCommand(
        String description,
        String driverId
) {

    public static CreateOrderCommand with(final String aDescription, final String aDriverId) {
        return new CreateOrderCommand(aDescription, aDriverId);
    }
}
