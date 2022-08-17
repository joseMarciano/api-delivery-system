package com.delivery.system.infrastructure.driver.presenters;

import com.delivery.system.application.driver.create.CreateDriverOutput;
import com.delivery.system.infrastructure.driver.models.create.CreateDriverResponse;

public interface DriverPresenters {

    static CreateDriverResponse present(CreateDriverOutput anOuput) {
        return new CreateDriverResponse(
                anOuput.id(),
                anOuput.name(),
                anOuput.createdAt(),
                anOuput.updatedAt()
        );
    }
}
