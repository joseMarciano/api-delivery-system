package com.delivery.system.application.driver.create;

import com.delivery.system.domain.driver.Driver;

import java.time.Instant;

public record CreateDriverOutput(
        String id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {

    public static CreateDriverOutput from(final Driver anDriver) {
        return new CreateDriverOutput(
                anDriver.getId().getValue(),
                anDriver.getName(),
                anDriver.getCreatedAt(),
                anDriver.getUpdatedAt()
        );
    }

}
