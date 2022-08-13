package com.delivery.system.infrastructure.driver.persistence;

import com.delivery.system.domain.driver.Driver;
import com.delivery.system.infrastructure.MYSQLGatewayTest;
import com.delivery.system.infrastructure.driver.DriverMySQLGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@MYSQLGatewayTest
public class DriverMySQLGatewayTest {

    @Autowired
    private DriverMySQLGateway driverMySQLGateway;

    @Autowired
    private DriverRepository driverRepository;

    @Test
    public void givenAValidDriver_whenCallsCreateDriver_shouldReturnDriverCreated() {
        final var aDriver = Driver.newDriver("John");

        Assertions.assertEquals(0, driverRepository.count());
        final var actualDriver = driverMySQLGateway.create(aDriver);
        Assertions.assertEquals(1, driverRepository.count());

        Assertions.assertEquals(aDriver.getId(), actualDriver.getId());
        Assertions.assertEquals(aDriver.getName(), actualDriver.getName());
        Assertions.assertNotNull(aDriver.getCreatedAt());
        Assertions.assertNotNull(aDriver.getUpdatedAt());
        Assertions.assertEquals(aDriver.getCreatedAt(), actualDriver.getCreatedAt());
        Assertions.assertEquals(aDriver.getUpdatedAt(), actualDriver.getUpdatedAt());

        final var actualEntity =
                driverRepository.findById(actualDriver.getId().getValue()).get();

        Assertions.assertEquals(actualEntity.getId(), actualDriver.getId().getValue());
        Assertions.assertEquals(actualEntity.getName(), actualDriver.getName());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertEquals(actualEntity.getCreatedAt(), actualDriver.getCreatedAt());
        Assertions.assertEquals(actualEntity.getUpdatedAt(), actualDriver.getUpdatedAt());
    }

    @Test
    public void givenAValidPrePersistedDriver_whenCallsUpdateDriver_shouldReturnDriverUpdated() {
        final var aDriver = Driver.newDriver("john");

        final var expectedName = "John";
        final var expectedId = aDriver.getId();

        Assertions.assertEquals(0, driverRepository.count());
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        Assertions.assertEquals(1, driverRepository.count());

        final var aDriverUpdated = aDriver.clone().update(expectedName);

        final var actualDriver = driverMySQLGateway
                .update(aDriverUpdated);

        assertEquals(actualDriver.getId(), expectedId);
        assertEquals(actualDriver.getName(), expectedName);
        assertNotNull(actualDriver.getCreatedAt());
        assertNotNull(actualDriver.getUpdatedAt());
        assertTrue(actualDriver.getUpdatedAt().isAfter(aDriver.getCreatedAt()));
    }

}
