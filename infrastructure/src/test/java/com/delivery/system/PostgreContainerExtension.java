package com.delivery.system;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;


public class PostgreContainerExtension implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void afterAll(ExtensionContext context) {
        final var container = PostgreContainer.getInstance();

        if (container.isRunning()) {
            container.stop();
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        final var container = PostgreContainer.getInstance();

        if (!container.isRunning()) {
            container.start();
        }

    }
}

class PostgreContainer extends PostgreSQLContainer<PostgreContainer> {

    private static final String IMAGE_VERSION = "postgres:12";
    private static final String DB_NAME = "order_manager";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "123";
    private static PostgreContainer container;

    private PostgreContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgreContainer getInstance() {
        if (container == null) {
            container = new PostgreContainer()
                    .withDatabaseName(DB_NAME)
                    .withUsername(USER_NAME)
                    .withPassword(PASSWORD);
        }
        return container;
    }
}
