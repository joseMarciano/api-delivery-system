package com.delivery.system.domain.exceptions;

import com.delivery.system.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {

    protected final List<Error> errors;

    protected DomainException(final String aMessage, List<Error> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException(anErrors != null ? anErrors.get(0).message() : "", anErrors);
    }

    public static DomainException with(final Error anError) {
        return DomainException.with(List.of(anError));
    }

    public List<Error> getErrors() {
        return errors;
    }
}
