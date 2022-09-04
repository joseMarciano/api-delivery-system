package com.delivery.system.infrastructure.listeners.order;

import com.delivery.system.application.order.updateStatus.UpdateStatusOrderCommand;
import com.delivery.system.application.order.updateStatus.UpdateStatusOrderUseCase;
import com.delivery.system.configs.json.Json;
import com.delivery.system.domain.order.StatusOrder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqOrderListener {


    private final UpdateStatusOrderUseCase updateStatusOrderUseCase;

    public RabbitMqOrderListener(final UpdateStatusOrderUseCase updateStatusOrderUseCase) {
        this.updateStatusOrderUseCase = updateStatusOrderUseCase;
    }

    @RabbitListener(id = "order.delivered", queues = "order.delivered")
    public void orderDeliveredListener(String anIn) {
        final var anInput = Json.readValue(anIn, OrderDeliveryListenerInput.class);
        updateStatusOrderUseCase.execute(UpdateStatusOrderCommand.with(anInput.value(), StatusOrder.DELIVERED));
    }

    //TODO: CRIAR ESTE TESTE
//    TODO: CRIAR TESTES DO GATEWAY DE ORDER
    @RabbitListener(id = "order.inProgress", queues = "order.inProgress")
    public void orderinProgresListener(String anIn) {
        final var anInput = Json.readValue(anIn, OrderInProgressListenerInput.class);
        updateStatusOrderUseCase.execute(UpdateStatusOrderCommand.with(anInput.value(), StatusOrder.IN_PROGRESS));
    }
}
