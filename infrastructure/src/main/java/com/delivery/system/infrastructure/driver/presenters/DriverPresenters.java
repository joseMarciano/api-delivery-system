package com.delivery.system.infrastructure.driver.presenters;

import com.delivery.system.application.driver.create.CreateDriverOutput;
import com.delivery.system.application.driver.findById.FindDriverByIdOutput;
import com.delivery.system.infrastructure.driver.models.create.CreateDriverResponse;
import com.delivery.system.infrastructure.driver.models.findById.FindDriverByIdResponse;

public interface DriverPresenters {

    static CreateDriverResponse present(CreateDriverOutput anOuput) {
        return new CreateDriverResponse(
                anOuput.id(),
                anOuput.name(),
                anOuput.createdAt(),
                anOuput.updatedAt()
        );
    }

    static FindDriverByIdResponse present(FindDriverByIdOutput anOutput) {
        return new FindDriverByIdResponse(
                anOutput.id(),
                anOutput.name(),
                anOutput.createdAt(),
                anOutput.updatedAt()
        );
    }
}
