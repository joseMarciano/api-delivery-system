package com.delivery.system;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

public class RabbitMQListenersCleanUpExtension implements BeforeEachCallback {

    private static Set<String> orderQueues = Set.of(
            "order.delivered",
            "order.inProgress"
    );


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final var rabbitAdmin = getRabbitAdmin(SpringExtension.getApplicationContext(context));
        final var listenerEndpointRegistry = getListenerRegistry(SpringExtension.getApplicationContext(context));

        listenerEndpointRegistry.stop();
        recreateQueues(rabbitAdmin);
    }

    private RabbitAdmin getRabbitAdmin(final ApplicationContext context) {
        return context.getBean(RabbitAdmin.class);
    }

    private RabbitListenerEndpointRegistry getListenerRegistry(final ApplicationContext context) {
        return context.getBean(RabbitListenerEndpointRegistry.class);
    }

    private void recreateQueues(final RabbitAdmin rabbitAdmin) {
        orderQueues.forEach(rabbitAdmin::deleteQueue);
        orderQueues.forEach(q -> rabbitAdmin.declareQueue(new Queue(q)));
    }
}
