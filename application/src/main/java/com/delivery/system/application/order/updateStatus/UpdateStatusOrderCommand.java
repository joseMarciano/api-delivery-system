package com.delivery.system.application.order.updateStatus;

import com.delivery.system.domain.order.StatusOrder;

public record UpdateStatusOrderCommand(
        String orderId,
        StatusOrder statusOrder
) {

    public static UpdateStatusOrderCommand with(final String orderId, final StatusOrder statusOrder) {
        return new UpdateStatusOrderCommand(orderId, statusOrder);
    }
}
