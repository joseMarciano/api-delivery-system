package com.delivery.system.infrastructure.api.order;

import com.delivery.system.application.order.create.CreateOrderCommand;
import com.delivery.system.application.order.create.CreateOrderUseCase;
import com.delivery.system.infrastructure.api.OrderAPI;
import com.delivery.system.infrastructure.order.models.create.CreateOrderRequest;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController implements OrderAPI {

    private final CreateOrderUseCase createOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @Override
    public void create(final CreateOrderRequest aRequest) {
        this.createOrderUseCase.execute(CreateOrderCommand.with(aRequest.name(), aRequest.driverId()));
    }

    @Override
    public List<?> create() {
        return null;
    }
}
