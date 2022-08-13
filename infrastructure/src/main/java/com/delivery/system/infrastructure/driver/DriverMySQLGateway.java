package com.delivery.system.infrastructure.driver;

import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.driver.DriverGateway;
import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.infrastructure.driver.persistence.DriverJpaEntity;
import com.delivery.system.infrastructure.driver.persistence.DriverRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DriverMySQLGateway implements DriverGateway {

    private final DriverRepository driverRepository;

    public DriverMySQLGateway(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Driver create(Driver aDriver) {
        return save(aDriver);
    }

    @Override
    public Driver update(Driver aDriver) {
        return save(aDriver);
    }

    @Override
    public Optional<Driver> findById(DriverID anId) {
        return driverRepository.findById(anId.getValue())
                .map(DriverJpaEntity::toAggregate);
    }

    @Override
    public void deleteById(DriverID anId) {
        if (driverRepository.existsById(anId.getValue())) {
            driverRepository.deleteById(anId.getValue());
        }
    }

    @Override
    public List<Driver> findAll() {
        return null;
    }

    private Driver save(Driver aDriver) {
        return driverRepository.save(DriverJpaEntity.from(aDriver)).toAggregate();
    }

}
