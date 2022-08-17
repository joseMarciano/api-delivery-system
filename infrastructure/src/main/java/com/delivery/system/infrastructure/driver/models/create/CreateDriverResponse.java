package com.delivery.system.infrastructure.driver.models.create;

import java.time.Instant;

public record CreateDriverResponse(
        String id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
}
