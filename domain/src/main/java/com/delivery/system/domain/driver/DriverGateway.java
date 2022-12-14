package com.delivery.system.domain.driver;

import java.util.List;
import java.util.Optional;

public interface DriverGateway {

    Driver create(Driver aDriver);

    Driver update(Driver aDriver);

    Optional<Driver> findById(DriverID anId);

    void deleteById(DriverID anId);

    List<Driver> findAll();

}
