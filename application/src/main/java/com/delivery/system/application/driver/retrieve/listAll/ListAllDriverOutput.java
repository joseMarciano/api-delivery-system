package com.delivery.system.application.driver.retrieve.listAll;

import com.delivery.system.domain.driver.Driver;

public record ListAllDriverOutput(
        String id,
        String name
) {

    public static ListAllDriverOutput from(final Driver aDriver) {
        return new ListAllDriverOutput(
                aDriver.getId().getValue(),
                aDriver.getName()
        );
    }

}
