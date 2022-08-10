package com.delivery.system.domain.driver;

import com.delivery.system.domain.AggregateRoot;
import com.delivery.system.domain.validation.ValidationHandler;
import com.delivery.system.domain.validation.handler.ThrowsValidationHandler;

import java.time.Instant;

public class Driver extends AggregateRoot<DriverID> {

    private String name;

    private Instant createdAt;

    private Instant updatedAt;

    public static Driver newDriver(final String aName) {
        final var anId = DriverID.unique();
        final var now = Instant.now();
        return new Driver(anId, aName, now, now);
    }

    private Driver(final DriverID anId, final String aName, Instant createdAt, Instant updatedAt) {
        super(anId);
        this.name = aName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        selfValidate();
    }

    @Override
    public void validate(ValidationHandler anHandler) {
        new DriverValidator(this, anHandler).validate();
    }

    public Driver update(final String name) {
        this.name = name;
        this.updatedAt = Instant.now();

        selfValidate();
        return this;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private void selfValidate() {
        validate(new ThrowsValidationHandler());
    }
}
