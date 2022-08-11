package com.delivery.system.application.driver.update;

public record UpdateDriverCommand(
        String id,
        String name
) {

    public static UpdateDriverCommand with(final String anId, final String aName) {
        return new UpdateDriverCommand(anId, aName);
    }
}
