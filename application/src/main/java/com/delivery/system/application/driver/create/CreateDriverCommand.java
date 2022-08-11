package com.delivery.system.application.driver.create;

public record CreateDriverCommand(
        String name
) {

    public static CreateDriverCommand with(final String aName) {
        return new CreateDriverCommand(aName);
    }
}
