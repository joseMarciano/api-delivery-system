package com.delivery.system.domain.order;

public interface OrderNotifier {

    void notifyCreated(NotifyOrderCreatedCommand aCommand);


    record NotifyOrderCreatedCommand(
            OrderID anId,
            String destiny
    ) {

        public static NotifyOrderCreatedCommand with(final OrderID anId, final String aDestiny) {
            return new NotifyOrderCreatedCommand(anId, aDestiny);
        }

    }

}
