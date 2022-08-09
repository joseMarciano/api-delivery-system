package com.delivery.system.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler anHandler);

    List<Error> getErrors();

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }


    default Error firstError() {
        return getErrors() != null && !getErrors().isEmpty()
                ? getErrors().get(0)
                : null;
    }

    interface Validation<T> {
        T validate();
    }

}
