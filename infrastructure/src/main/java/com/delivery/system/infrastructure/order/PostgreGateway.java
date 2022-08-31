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
public class PostgreGateway implements OrderGateway {

    private final OrderRepository orderRepository;

    public PostgreGateway(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order create(Order aOrder) {
        return this.orderRepository.save(OrderJpaEntity.from(aOrder)).toAggregate();
    }

    @Override
    public Order update(Order aOrder) {
        return null;
    }

    @Override
    public Optional<Order> findById(OrderID anId) {
        throw new RuntimeException("Need to be implemented");
    }

    @Override
    public void deleteById(OrderID anId) {
        throw new RuntimeException("Need to be implemented");
    }

    @Override
    public List<Order> findAll() {
        return this.orderRepository.findAll().stream().map(OrderJpaEntity::toAggregate).toList();
    }
}
