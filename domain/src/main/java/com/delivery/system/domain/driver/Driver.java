package com.delivery.system.domain.driver;

import com.delivery.system.domain.AggregateRoot;
import com.delivery.system.domain.validation.ValidationHandler;
import com.delivery.system.domain.validation.handler.ThrowsValidationHandler;

import java.time.Instant;

public class Driver extends AggregateRoot<DriverID> {

    private String name;

    private String cpf;

    private Instant createdAt;

    private Instant updatedAt;

    public static Driver newDriver(final String aName, final String aCpf) {
        final var anId = DriverID.unique();
        final var now = Instant.now();
        return new Driver(anId, aName, aCpf, now, now);
    }

    private Driver(final DriverID anId, final String aName, final String aCpf, Instant createdAt, Instant updatedAt) {
        super(anId);
        this.name = aName;
        this.cpf = aCpf;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        selfValidate();
    }

    @Override
    public void validate(ValidationHandler anHandler) {
        new DriverValidator(this, anHandler).validate();
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
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
