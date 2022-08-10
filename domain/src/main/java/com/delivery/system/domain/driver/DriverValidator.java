package com.delivery.system.domain.driver;

import com.delivery.system.domain.validation.Error;
import com.delivery.system.domain.validation.ValidationHandler;
import com.delivery.system.domain.validation.Validator;

import static java.util.Objects.isNull;

public class DriverValidator extends Validator {
    private static int MIN_SIZE_NAME = 1;
    private static int MAX_SIZE_NAME = 255;
    private Driver driver;

    protected DriverValidator(final Driver anDriver, final ValidationHandler handler) {
        super(handler);
        this.driver = anDriver;
    }

    @Override
    public void validate() {
        validateName();
    }

    private void validateName() {
        final var actualName = this.driver.getName();

        if (isNull(actualName) || actualName.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be null"));
        }

        if (actualName.length() < MIN_SIZE_NAME || actualName.length() > MAX_SIZE_NAME) {
            this.validationHandler()
                    .append(new Error(
                            "'name' should be between %s and %s characters"
                                    .formatted(MIN_SIZE_NAME, MAX_SIZE_NAME)
                    ));
        }
    }
}
