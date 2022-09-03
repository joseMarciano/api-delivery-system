package com.delivery.system.configs.order;

import com.delivery.system.application.order.create.CreateOrderUseCase;
import com.delivery.system.application.order.create.DefaultCreateOrderNotifierUseCase;
import com.delivery.system.application.order.create.DefaultCreateOrderUseCase;
import com.delivery.system.application.order.retrieve.listAll.DefaultListAllOrdersUseCase;
import com.delivery.system.application.order.retrieve.listAll.ListAllOrdersUseCase;
import com.delivery.system.domain.order.OrderGateway;
import com.delivery.system.domain.order.OrderNotifier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigOrderUseCase {

    private final OrderGateway orderGateway;
    private final OrderNotifier orderNotifier;

    public ConfigOrderUseCase(final OrderGateway orderGateway, final OrderNotifier orderNotifier) {
        this.orderGateway = orderGateway;
        this.orderNotifier = orderNotifier;
    }

    @Bean
    @Qualifier("createOrderUseCase")
    public CreateOrderUseCase createOrderUseCase() {
        return new DefaultCreateOrderUseCase(orderGateway);
    }

    @Bean
    @Qualifier("createOrderUseCaseNotifier")
    public CreateOrderUseCase createOrderUseCaseNotifier(@Qualifier("createOrderUseCase") final CreateOrderUseCase createOrderUseCase) {
        return new DefaultCreateOrderNotifierUseCase(createOrderUseCase, orderNotifier);
    }

    @Bean
    public ListAllOrdersUseCase listAllOrdersUseCase() {
        return new DefaultListAllOrdersUseCase(orderGateway);
    }
}
