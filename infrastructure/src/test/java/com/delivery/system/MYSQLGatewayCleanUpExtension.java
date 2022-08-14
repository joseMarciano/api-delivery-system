package com.delivery.system;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class MYSQLGatewayCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final var applicationContext = SpringExtension.getApplicationContext(context);

        cleanUpGateway(applicationContext);
    }

    private void cleanUpGateway(ApplicationContext applicationContext) {
        applicationContext.getBeansOfType(CrudRepository.class)
                .forEach((s, crudRepository) -> crudRepository.deleteAll());
    }
}
