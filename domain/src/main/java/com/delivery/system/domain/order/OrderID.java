package com.delivery.system.domain.order;

import com.delivery.system.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class OrderID extends Identifier {

    private String value;

    public static OrderID unique() {
        return new OrderID(UUID.randomUUID().toString());
    }

    private OrderID(String aValue) {
        Objects.requireNonNull(aValue, "aValue is required");
        this.value = aValue;
    }

    public static OrderID from(final String anId) {
        return new OrderID(anId);
    }


    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var driverID = (OrderID) o;
        return getValue().equals(driverID.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
