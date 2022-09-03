package com.delivery.system.infrastructure.order;

import com.delivery.system.configs.json.Json;
import com.delivery.system.domain.order.OrderID;
import com.delivery.system.domain.order.OrderNotifier;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqNotifier implements OrderNotifier {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqNotifier(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public void notifyCreated(OrderID anId) {
        rabbitTemplate.convertAndSend("order.created", Json.writeValueAsString(anId));
    }
}
