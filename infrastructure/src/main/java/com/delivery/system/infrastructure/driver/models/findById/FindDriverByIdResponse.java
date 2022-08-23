package com.delivery.system.infrastructure.driver.models.findById;

import java.time.Instant;

public record FindDriverByIdResponse(
        String id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {


}
