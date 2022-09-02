package com.delivery.system.domain.order;

public interface OrderNotifier {

    Order notifyCreated(OrderID anId);

}
