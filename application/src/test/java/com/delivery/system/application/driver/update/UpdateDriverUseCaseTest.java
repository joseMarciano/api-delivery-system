package com.delivery.system.application.driver.update;


import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.driver.DriverGateway;
import com.delivery.system.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateDriverUseCaseTest {

    @InjectMocks
    private DefaultUpdateDriverUseCase useCase;

    @Mock
    private DriverGateway driverGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateDriver_shouldReturnDriverUpdated() {
        final var aDriver = Driver.newDriver("john");

        final var expectedName = "John";
        final var expectedId = aDriver.getId();

        final var aCommand = UpdateDriverCommand.with(expectedId.getValue(), expectedName);


        when(driverGateway.findById(eq(expectedId))).thenReturn(Optional.of(aDriver.clone()));
        when(driverGateway.update(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(actualOutput.id(), expectedId.getValue());
        assertEquals(actualOutput.name(), expectedName);
        assertNotNull(actualOutput.createdAt());
        assertNotNull(actualOutput.updatedAt());
        assertTrue(actualOutput.createdAt().isBefore(actualOutput.updatedAt()));

        verify(driverGateway, times(1)).findById(expectedId);
        verify(driverGateway, times(1)).update(any());

    }

    @Test
    public void givenAInvalidCommandWithEmptyName_whenCallsUpdateDriver_shouldReturnDomainException() {
        final var aDriver = Driver.newDriver("john");

        final var expectedName = "  ";
        final var expectedId = aDriver.getId();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorsCount = 1;

        final var aCommand = UpdateDriverCommand.with(expectedId.getValue(), expectedName);

        when(driverGateway.findById(eq(expectedId))).thenReturn(Optional.of(aDriver.clone()));
        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(actualException.getMessage(), expectedErrorMessage);
        assertEquals(actualException.getErrors().size(), expectedErrorsCount);

        verify(driverGateway, times(0)).update(any());
        verify(driverGateway, times(1)).findById(expectedId);

    }

    @Test
    public void givenAInvalidCommandWithNullName_whenCallsUpdateDriver_shouldReturnDomainException() {
        final var aDriver = Driver.newDriver("john");

        final String expectedName = null;
        final var expectedId = aDriver.getId();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorsCount = 1;

        final var aCommand = UpdateDriverCommand.with(expectedId.getValue(), expectedName);

        when(driverGateway.findById(eq(expectedId))).thenReturn(Optional.of(aDriver.clone()));
        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(actualException.getMessage(), expectedErrorMessage);
        assertEquals(actualException.getErrors().size(), expectedErrorsCount);

        verify(driverGateway, times(0)).update(any());
        verify(driverGateway, times(1)).findById(expectedId);

    }

    @Test
    public void givenAValidCommand_whenCallsUpdateDriverAndGatewayCreateThrowsRandomException_shouldReturnARandomException() {
        final var aDriver = Driver.newDriver("john");

        final var expectedName = "John";
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = aDriver.getId();

        final var aCommand = UpdateDriverCommand.with(expectedId.getValue(), expectedName);


        when(driverGateway.findById(eq(expectedId))).thenReturn(Optional.of(aDriver.clone()));
        when(driverGateway.update(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualExpcetion = assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));

        assertEquals(actualExpcetion.getMessage(), expectedErrorMessage);
        verify(driverGateway, times(1)).update(any());
        verify(driverGateway, times(1)).findById(any());

    }

    @Test
    public void givenAValidCommand_whenCallsUpdateDriverAndGatewayFindByIdThrowsRandomException_shouldReturnARandomException() {
        final var aDriver = Driver.newDriver("john");

        final var expectedName = "John";
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = aDriver.getId();

        final var aCommand = UpdateDriverCommand.with(expectedId.getValue(), expectedName);

        when(driverGateway.findById(expectedId)).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualExpcetion = assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));

        assertEquals(actualExpcetion.getMessage(), expectedErrorMessage);
        verify(driverGateway, times(0)).update(any());
        verify(driverGateway, times(1)).findById(any());

    }

}
