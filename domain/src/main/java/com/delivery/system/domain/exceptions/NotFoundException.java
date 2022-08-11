package com.delivery.system.domain.exceptions;

import com.delivery.system.domain.Entity;
import com.delivery.system.domain.Identifier;
import com.delivery.system.domain.validation.Error;

import java.util.List;

public class NotFoundException extends DomainException {


    private NotFoundException(String aMessage, List<Error> anErrors) {
        super(aMessage, anErrors);
    }

    public static NotFoundException with(Identifier anId, Class<? extends Entity> clazz) {
        return new NotFoundException("%s with identifier %s was not found".formatted(clazz.getSimpleName(), anId.getValue()), List.of());
    }
}
