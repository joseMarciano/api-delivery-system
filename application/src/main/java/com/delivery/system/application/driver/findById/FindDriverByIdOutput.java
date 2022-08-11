package com.delivery.system.application.driver.findById;

import com.delivery.system.domain.driver.Driver;

import java.time.Instant;

public record FindDriverByIdOutput(
        String id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {

    public static FindDriverByIdOutput from(final Driver anDriver) {
        return new FindDriverByIdOutput(
                anDriver.getId().getValue(),
                anDriver.getName(),
                anDriver.getCreatedAt(),
                anDriver.getUpdatedAt()
        );
    }

}
