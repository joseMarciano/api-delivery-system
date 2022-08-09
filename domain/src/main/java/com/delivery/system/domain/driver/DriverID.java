package com.delivery.system.domain.driver;

import com.delivery.system.domain.Identifier;

import java.util.UUID;

public class DriverID extends Identifier {

    private String value;

    public static DriverID unique() {
        return new DriverID(UUID.randomUUID().toString());
    }

    private DriverID(String aValue) {
        this.value = aValue;
    }


    @Override
    public String getValue() {
        return value;
    }
}
