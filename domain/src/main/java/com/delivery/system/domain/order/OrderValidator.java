package com.delivery.system.domain.order;

import com.delivery.system.domain.validation.Error;
import com.delivery.system.domain.validation.ValidationHandler;
import com.delivery.system.domain.validation.Validator;

import static java.util.Objects.isNull;

public class OrderValidator extends Validator {
    private static int MIN_SIZE_DESCRIPTION = 1;
    private static int MAX_SIZE_DESCRIPTION = 255;
    private Order order;

    protected OrderValidator(final Order anOrder, final ValidationHandler handler) {
        super(handler);
        this.order = anOrder;
    }

    @Override
    public void validate() {
        validateDescription();
        validateDriver();
        validateStatusOrder();
    }

    private void validateDescription() {
        final var actualDescription = this.order.getDescription();

        if (isNull(actualDescription) || actualDescription.isBlank()) {
            this.validationHandler().append(new Error("'description' should not be null"));
        }

        if (actualDescription.length() < MIN_SIZE_DESCRIPTION || actualDescription.length() > MAX_SIZE_DESCRIPTION) {
            this.validationHandler()
                    .append(new Error(
                            "'description' should be between %s and %s characters"
                                    .formatted(MIN_SIZE_DESCRIPTION, MAX_SIZE_DESCRIPTION)
                    ));
        }
    }

    private void validateDriver() {
        final var actualDriverId = this.order.getDriverID();

        if (isNull(actualDriverId)) {
            this.validationHandler().append(new Error("'driverId' should not be null"));
        }

    }

    private void validateStatusOrder() {
        final var actualStatusOrder = this.order.getStatus();

        if (isNull(actualStatusOrder)) {
            this.validationHandler().append(new Error("'statusOrder' should not be null"));
        }
    }
}
