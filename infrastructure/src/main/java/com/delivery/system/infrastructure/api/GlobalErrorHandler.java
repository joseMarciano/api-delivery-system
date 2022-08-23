package com.delivery.system.infrastructure.api;


import com.delivery.system.domain.exceptions.DomainException;
import com.delivery.system.domain.validation.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(value = {DomainException.class})
    public ResponseEntity<?> domainExceptionHandler(final DomainException domainException) {
        final var apiError = ApiError
                .with(domainException.getMessage(), domainException.getErrors());
        return ResponseEntity.unprocessableEntity().body(apiError);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> generalExceptionHandler(final Exception exception) {
        final var apiError = ApiError
                .with("Internal server error", List.of(new Error(exception.getMessage())));
        return ResponseEntity.unprocessableEntity().body(apiError);
    }

    record ApiError(String message, List<Error> errors) {
        public static ApiError with(final String message, final List<Error> errors) {
            return new ApiError(message, errors);
        }

    }
}
