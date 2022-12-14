package com.delivery.system.domain.driver;

import com.delivery.system.domain.AggregateRoot;
import com.delivery.system.domain.validation.ValidationHandler;
import com.delivery.system.domain.validation.handler.ThrowsValidationHandler;
import com.delivery.system.util.InstantUtils;

import java.time.Instant;

public class Driver extends AggregateRoot<DriverID> implements Cloneable {

    private String name;

    private Instant createdAt;

    private Instant updatedAt;

    public static Driver newDriver(final String aName) {
        final var anId = DriverID.unique();
        final var now = InstantUtils.now();
        return new Driver(anId, aName, now, now);
    }

    private Driver(final DriverID anId, final String aName, Instant createdAt, Instant updatedAt) {
        super(anId);
        this.name = aName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        selfValidate();
    }

    public static Driver with(final DriverID anId,
                              final String aName,
                              final Instant createdAt,
                              final Instant updatedAt) {
        return new Driver(
                anId,
                aName,
                createdAt,
                updatedAt
        );
    }

    @Override
    public void validate(ValidationHandler anHandler) {
        new DriverValidator(this, anHandler).validate();
    }

    public Driver update(final String name) {
        this.name = name;
        this.updatedAt = InstantUtils.now();

        selfValidate();
        return this;
    }

    @Override
    public Driver clone() {
        try {
            return (Driver) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
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
