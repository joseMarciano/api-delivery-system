package com.delivery.system.infrastructure.api.order;

import com.delivery.system.application.order.create.CreateOrderCommand;
import com.delivery.system.application.order.create.CreateOrderUseCase;
import com.delivery.system.application.order.retrieve.listAll.ListAllOrdersUseCase;
import com.delivery.system.infrastructure.api.OrderAPI;
import com.delivery.system.infrastructure.order.models.create.CreateOrderRequest;
import com.delivery.system.infrastructure.order.models.listAll.ListAllOrdersResponse;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController implements OrderAPI {

    private final CreateOrderUseCase createOrderUseCase;
    private final ListAllOrdersUseCase listAllOrdersUseCase;

    public OrderController(final CreateOrderUseCase createOrderUseCase,
                           final ListAllOrdersUseCase listAllOrdersUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.listAllOrdersUseCase = listAllOrdersUseCase;
    }

    @Override
    public void create(final CreateOrderRequest aRequest) {
        this.createOrderUseCase.execute(CreateOrderCommand.with(aRequest.description(), aRequest.driverId()));
    }

    @Override
    public List<ListAllOrdersResponse> listAll() {
        return this.listAllOrdersUseCase.execute().stream().map(ListAllOrdersResponse::from).toList();
    }
}
