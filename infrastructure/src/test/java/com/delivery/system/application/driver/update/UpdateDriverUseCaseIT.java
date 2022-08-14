package com.delivery.system.application.driver.update;


import com.delivery.system.IntegrationTest;
import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.exceptions.DomainException;
import com.delivery.system.infrastructure.driver.persistence.DriverJpaEntity;
import com.delivery.system.infrastructure.driver.persistence.DriverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class UpdateDriverUseCaseIT {

    @Autowired
    private DefaultUpdateDriverUseCase useCase;

    @Autowired
    private DriverRepository driverRepository;

    @Test
    public void givenAValidCommand_whenCallsUpdateDriver_shouldReturnDriverUpdated() {
        final var aDriver = Driver.newDriver("john");
        final var expectedName = "John";
        final var expectedId = aDriver.getId();

        assertEquals(0, driverRepository.count());
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        assertEquals(1, driverRepository.count());

        final var aCommand = UpdateDriverCommand.with(expectedId.getValue(), expectedName);

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(actualOutput.id(), expectedId.getValue());
        assertEquals(actualOutput.name(), expectedName);
        assertNotNull(actualOutput.createdAt());
        assertNotNull(actualOutput.updatedAt());
        assertTrue(actualOutput.createdAt().isBefore(actualOutput.updatedAt()));

        final var actualEntity = driverRepository.findById(actualOutput.id()).get();


        assertEquals(actualOutput.id(), actualEntity.getId());
        assertEquals(actualOutput.name(), actualEntity.getName());
        assertEquals(actualOutput.createdAt(), actualEntity.getCreatedAt());
        assertEquals(actualOutput.updatedAt(), actualEntity.getUpdatedAt());

    }

    @Test
    public void givenAInvalidCommandWithEmptyName_whenCallsUpdateDriver_shouldReturnDomainException() {
        final var aDriver = Driver.newDriver("john");

        final var expectedName = "  ";
        final var expectedId = aDriver.getId();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorsCount = 1;

        assertEquals(0, driverRepository.count());
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        assertEquals(1, driverRepository.count());


        final var aCommand = UpdateDriverCommand.with(expectedId.getValue(), expectedName);

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(actualException.getMessage(), expectedErrorMessage);
        assertEquals(actualException.getErrors().size(), expectedErrorsCount);

        final var actualEntity = driverRepository.findById(expectedId.getValue()).get();


        assertEquals(aDriver.getId().getValue(), actualEntity.getId());
        assertEquals(aDriver.getName(), actualEntity.getName());
        assertEquals(aDriver.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(aDriver.getUpdatedAt(), actualEntity.getUpdatedAt());

    }

    @Test
    public void givenAInvalidCommandWithNullName_whenCallsUpdateDriver_shouldReturnDomainException() {
        final var aDriver = Driver.newDriver("john");

        final String expectedName = null;
        final var expectedId = aDriver.getId();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorsCount = 1;

        assertEquals(0, driverRepository.count());
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        assertEquals(1, driverRepository.count());


        final var aCommand = UpdateDriverCommand.with(expectedId.getValue(), expectedName);

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(actualException.getMessage(), expectedErrorMessage);
        assertEquals(actualException.getErrors().size(), expectedErrorsCount);

        final var actualEntity = driverRepository.findById(expectedId.getValue()).get();


        assertEquals(aDriver.getId().getValue(), actualEntity.getId());
        assertEquals(aDriver.getName(), actualEntity.getName());
        assertEquals(aDriver.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(aDriver.getUpdatedAt(), actualEntity.getUpdatedAt());
    }
}
