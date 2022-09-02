package com.delivery.system.application.order.create;

import com.delivery.system.domain.order.OrderID;
import com.delivery.system.domain.order.OrderNotifier;

import java.util.Objects;

public class DefaultCreateOrderNotifierUseCase extends CreateOrderUseCase {

    private final CreateOrderUseCase orderUseCase;
    private final OrderNotifier notifier;

    public DefaultCreateOrderNotifierUseCase(final CreateOrderUseCase orderUseCase,
                                             final OrderNotifier notifier) {
        this.orderUseCase = Objects.requireNonNull(orderUseCase, "orderUseCase must not be null.");
        this.notifier = Objects.requireNonNull(notifier, "notifier must not be null.");
    }

    @Override
    public CreateOrderOuput execute(CreateOrderCommand anIn) {
        final var anOutput = orderUseCase.execute(anIn);
        notifier.notifyCreated(OrderID.from(anOutput.id()));
        return anOutput;
    }

}
