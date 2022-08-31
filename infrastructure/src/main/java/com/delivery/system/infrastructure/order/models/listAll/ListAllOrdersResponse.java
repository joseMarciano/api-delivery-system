package com.delivery.system.infrastructure.order.models.listAll;

import com.delivery.system.application.order.retrieve.listAll.ListAllOrdersOutput;
import com.delivery.system.domain.order.StatusOrder;

import java.time.Instant;

public record ListAllOrdersResponse(
        String id,
        String description,
        StatusOrder statusOrder,
        Instant deliveredAt
) {
    public static ListAllOrdersResponse from(final ListAllOrdersOutput anOuput) {
        return new ListAllOrdersResponse(
                anOuput.id(),
                anOuput.description(),
                anOuput.statusOrder(),
                anOuput.deliveredAt()
        );
    }
}
