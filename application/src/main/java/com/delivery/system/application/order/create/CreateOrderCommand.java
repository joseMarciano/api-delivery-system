package com.delivery.system.application.order.create;

public record CreateOrderCommand(
        String description,
        String driverId,
        String destiny
) {

    public static CreateOrderCommand with(final String aDescription, final String aDriverId, final String aDestiny) {
        return new CreateOrderCommand(aDescription, aDriverId, aDestiny);
    }
}
