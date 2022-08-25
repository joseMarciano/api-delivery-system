package com.delivery.system.application.order.updateStatus;

import com.delivery.system.domain.order.OrderGateway;
import com.delivery.system.domain.order.OrderID;

public class DefaultUpdateStatusOrderUseCase extends UpdateStatusOrderUseCase {

    private final OrderGateway orderGateway;

    public DefaultUpdateStatusOrderUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Override
    public void execute(UpdateStatusOrderCommand aCommand) {
        final var optionalOrder =
                orderGateway.findById(OrderID.from(aCommand.orderId()));

        if (optionalOrder.isEmpty()) return;

        final var anOrder = optionalOrder.get();

        orderGateway.update(anOrder.changeStatus(aCommand.statusOrder()));
    }
}
