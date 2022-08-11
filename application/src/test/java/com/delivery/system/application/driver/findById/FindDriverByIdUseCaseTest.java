package com.delivery.system.application.driver.findById;

import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.driver.DriverGateway;
import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class FindDriverByIdUseCaseTest {

    @InjectMocks
    private DefaultFindDriverByIdUseCase useCase;

    @Mock
    private DriverGateway driverGateway;


    @Test
    public void givenAValidCommand_whenCallsFindById_shouldReturnADriver() {
        final var expectedName = "John";
        final var aDriver = Driver.newDriver(expectedName);
        final var expectedId = aDriver.getId();

        Mockito.when(driverGateway.findById(expectedId)).thenReturn(Optional.of(aDriver));

        final var aCommand = FindDriverByIdCommand.with(expectedId.getValue());

        final var actualDriver = useCase.execute(aCommand);

        assertEquals(actualDriver.name(), expectedName);
        assertEquals(actualDriver.id(), expectedId.getValue());
        assertNotNull(actualDriver.updatedAt());
        assertNotNull(actualDriver.createdAt());
    }

    @Test
    public void givenAInvalidCommandWithNoExistingDriver_whenCallsFindById_shouldReturnNotFoundException() {
        final var expectedId = DriverID.unique();

        final var expectedErrorMessage = "Driver with identifier %s was not found".formatted(expectedId.getValue());

        final var aCommand = FindDriverByIdCommand.with(expectedId.getValue());

        final var actualOuput = assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        assertEquals(actualOuput.getMessage(), expectedErrorMessage);
        Mockito.verify(driverGateway).findById(any());
    }

    @Test
    public void givenAValid_whenCallsFindByIdAndatewayThrowsRandomException_shouldReturnRandomException() {
        final var expectedName = "John";
        final var aDriver = Driver.newDriver(expectedName);
        final var expectedId = aDriver.getId();
        final var expectedErrorMessage = "Gateway error";

        Mockito.when(driverGateway.findById(expectedId)).thenThrow(new IllegalStateException("Gateway error"));
        final var aCommand = FindDriverByIdCommand.with(expectedId.getValue());

        final var actualOuput = assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));

        assertEquals(actualOuput.getMessage(), expectedErrorMessage);
        Mockito.verify(driverGateway).findById(any());
    }

}
