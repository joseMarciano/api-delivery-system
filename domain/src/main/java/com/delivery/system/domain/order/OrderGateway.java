package com.delivery.system.domain.order;

import java.util.List;
import java.util.Optional;

public interface OrderGateway {

    Order create(Order aOrder);

    Order update(Order aOrder);

    Optional<Order> findById(OrderID anId);

    void deleteById(OrderID anId);

    List<Order> findAll();

}
