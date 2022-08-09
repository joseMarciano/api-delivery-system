package com.delivery.system.domain.validation.handler;

import com.delivery.system.domain.exceptions.DomainException;
import com.delivery.system.domain.validation.Error;
import com.delivery.system.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(ValidationHandler anHandler) {
        throw DomainException.with(anHandler.getErrors());
    }

    @Override
    public List<Error> getErrors() {
        return null;
    }
}
