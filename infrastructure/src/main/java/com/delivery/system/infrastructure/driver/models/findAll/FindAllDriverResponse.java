package com.delivery.system.infrastructure.driver.models.findAll;

import com.delivery.system.application.driver.retrieve.listAll.ListAllDriverOutput;

public record FindAllDriverResponse(String id,
                                    String name
) {

    public static FindAllDriverResponse with(ListAllDriverOutput anOutput) {
        return new FindAllDriverResponse(anOutput.id(), anOutput.name());
    }

}
