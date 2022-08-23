package com.delivery.system.infrastructure.driver.presenters;

import com.delivery.system.application.driver.create.CreateDriverOutput;
import com.delivery.system.application.driver.findById.FindDriverByIdOutput;
import com.delivery.system.application.driver.retrieve.listAll.ListAllDriverOutput;
import com.delivery.system.infrastructure.driver.models.create.CreateDriverResponse;
import com.delivery.system.infrastructure.driver.models.findAll.FindAllDriverResponse;
import com.delivery.system.infrastructure.driver.models.findById.FindDriverByIdResponse;

import java.util.List;
import java.util.function.Function;

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

    static List<FindAllDriverResponse> present(List<ListAllDriverOutput> outputs) {
        return mapTo(outputs, FindAllDriverResponse::with);
    }

    private static <I, O> List<O> mapTo(final List<I> list, final Function<I, O> mapper) {
        return list.stream().map(mapper).toList();
    }
}
