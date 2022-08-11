package com.delivery.system.domain.driver;

import com.delivery.system.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class DriverID extends Identifier {

    private String value;

    public static DriverID unique() {
        return new DriverID(UUID.randomUUID().toString());
    }

    private DriverID(String aValue) {
        Objects.requireNonNull(aValue, "aValue is required");
        this.value = aValue;
    }

    public static DriverID from(final String anId) {
        return new DriverID(anId);
    }


    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var driverID = (DriverID) o;
        return getValue().equals(driverID.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
