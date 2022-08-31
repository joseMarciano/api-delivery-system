package com.delivery.system.configs.order;

import com.delivery.system.application.order.create.CreateOrderUseCase;
import com.delivery.system.application.order.create.DefaultCreateOrderUseCase;
import com.delivery.system.application.order.retrieve.listAll.DefaultListAllOrdersUseCase;
import com.delivery.system.application.order.retrieve.listAll.ListAllOrdersUseCase;
import com.delivery.system.domain.order.OrderGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigOrderUseCase {

    private final OrderGateway orderGateway;

    public ConfigOrderUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Bean
    public CreateOrderUseCase createOrderUseCase() {
        return new DefaultCreateOrderUseCase(orderGateway);
    }

    @Bean
    public ListAllOrdersUseCase listAllOrdersUseCase() {
        return new DefaultListAllOrdersUseCase(orderGateway);
    }
}
