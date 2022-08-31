package com.delivery.system.application.order.create;

import com.delivery.system.domain.order.Order;

public record CreateOrderOuput(
        String id
) {
    public static CreateOrderOuput from(final Order aOrder) {
        return new CreateOrderOuput(aOrder.getId().getValue());
    }
}
