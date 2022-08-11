package com.delivery.system.application.driver.deleteById;

public record DeleteDriverByIdCommand(String id) {

    public static DeleteDriverByIdCommand with(final String anId) {
        return new DeleteDriverByIdCommand(anId);
    }
}
