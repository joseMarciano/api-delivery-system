package com.delivery.system.application.driver.update;

import com.delivery.system.domain.driver.Driver;

import java.time.Instant;

public record UpdateDriverOutput(
        String id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {

    public static UpdateDriverOutput from(final Driver aDriver) {
        return new UpdateDriverOutput(
                aDriver.getId().getValue(),
                aDriver.getName(),
                aDriver.getCreatedAt(),
                aDriver.getUpdatedAt()
        );
    }
}
