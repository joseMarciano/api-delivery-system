package com.delivery.system.application.driver.retrieve.listAll;

import com.delivery.system.domain.driver.DriverGateway;

import java.util.List;

public class DefaultListAllDriverUseCase extends ListAllDriverUseCase {

    private final DriverGateway driverGateway;

    public DefaultListAllDriverUseCase(DriverGateway driverGateway) {
        this.driverGateway = driverGateway;
    }


    @Override
    public List<ListAllDriverOutput> execute() {
        return driverGateway.findAll().stream().map(ListAllDriverOutput::from).toList();
    }
}
