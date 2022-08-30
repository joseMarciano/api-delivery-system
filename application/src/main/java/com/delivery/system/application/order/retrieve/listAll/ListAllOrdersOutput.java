package com.delivery.system.application.order.retrieve.listAll;

import com.delivery.system.domain.order.Order;

public record ListAllOrdersOutput(
        String id,
        String description
) {
    public static ListAllOrdersOutput from(final Order anOrder) {
        return new ListAllOrdersOutput(anOrder.getId().getValue(), anOrder.getDescription());
    }
}
