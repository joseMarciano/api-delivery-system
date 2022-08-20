package com.delivery.system;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class FlyWayMigrationExtension implements BeforeEachCallback {


    @Override
    public void beforeEach(ExtensionContext context) {
        final var applicationContext =
                SpringExtension.getApplicationContext(context);

        final var flyway = applicationContext.getBean(Flyway.class);

        flyway.clean();
        flyway.migrate();
    }
}
