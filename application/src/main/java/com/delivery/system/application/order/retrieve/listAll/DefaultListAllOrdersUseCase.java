package com.delivery.system.application.order.retrieve.listAll;

import com.delivery.system.domain.order.OrderGateway;

import java.util.List;

public class DefaultListAllOrdersUseCase extends ListAllOrdersUseCase {

    private final OrderGateway orderGateway;

    public DefaultListAllOrdersUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Override
    public List<ListAllOrdersOutput> execute() {
        return orderGateway.findAll().stream().map(ListAllOrdersOutput::from).toList();
    }
}
