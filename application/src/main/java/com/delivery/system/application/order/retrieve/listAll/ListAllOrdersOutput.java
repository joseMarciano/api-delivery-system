package com.delivery.system.application.order.retrieve.listAll;

import com.delivery.system.domain.order.Order;
import com.delivery.system.domain.order.StatusOrder;

import java.time.Instant;

public record ListAllOrdersOutput(
        String id,
        String description,
        StatusOrder statusOrder,
        Instant deliveredAt
) {
    public static ListAllOrdersOutput from(final Order anOrder) {
        return new ListAllOrdersOutput(
                anOrder.getId().getValue(),
                anOrder.getDescription(),
                anOrder.getStatus(),
                anOrder.getTimeDelivered());
    }
}
