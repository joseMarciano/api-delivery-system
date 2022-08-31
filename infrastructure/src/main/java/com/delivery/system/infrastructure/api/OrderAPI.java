package com.delivery.system.infrastructure.api;

import com.delivery.system.infrastructure.order.models.create.CreateOrderRequest;
import com.delivery.system.infrastructure.order.models.listAll.ListAllOrdersResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/orders")
public interface OrderAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    void create(@RequestBody CreateOrderRequest createOrderRequest);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    List<ListAllOrdersResponse> listAll();


}
