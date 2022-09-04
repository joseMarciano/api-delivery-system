package com.delivery.system.infrastructure.order;

import com.delivery.system.domain.order.Order;
import com.delivery.system.domain.order.OrderGateway;
import com.delivery.system.domain.order.OrderID;
import com.delivery.system.infrastructure.order.persistence.OrderJpaEntity;
import com.delivery.system.infrastructure.order.persistence.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderPostgreGateway implements OrderGateway {

    private final OrderRepository orderRepository;

    public OrderPostgreGateway(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order create(Order aOrder) {
        return save(aOrder);
    }

    @Override
    public Order update(Order aOrder) {
        return save(aOrder);
    }

    @Override
    public Optional<Order> findById(OrderID anId) {
        return this.orderRepository.findById(anId.getValue()).map(OrderJpaEntity::toAggregate);
    }

    @Override
    public void deleteById(OrderID anId) {
        throw new RuntimeException("Need to be implemented");
    }

    @Override
    public List<Order> findAll() {
        return this.orderRepository.findAll().stream().map(OrderJpaEntity::toAggregate).toList();
    }

    private Order save(Order aOrder) {
        return this.orderRepository.save(OrderJpaEntity.from(aOrder)).toAggregate();
    }
}
