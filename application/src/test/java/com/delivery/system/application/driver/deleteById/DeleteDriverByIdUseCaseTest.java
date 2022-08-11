package com.delivery.system.application.driver.deleteById;

import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.driver.DriverGateway;
import com.delivery.system.domain.driver.DriverID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteDriverByIdUseCaseTest {

    @InjectMocks
    private DefaultDeleteDriverByIdUseCase useCase;

    @Mock
    private DriverGateway driverGateway;


    @Test
    public void givenAValidCommand_whenCallsDeleteDriver_shouldBeOk() {
        final var expectedName = "John";
        final var aDriver = Driver.newDriver(expectedName);
        final var expectedId = aDriver.getId();

        doNothing().when(driverGateway).deleteById(expectedId);

        final var aCommand = DeleteDriverByIdCommand.with(expectedId.getValue());

        assertDoesNotThrow(() -> useCase.execute(aCommand));
        verify(driverGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAValidCommandWithNoExistingDriver_whenCallsDeleteDriver_shouldBeOk() {
        final var expectedId = DriverID.from("not-existing-id");

        doNothing().when(driverGateway).deleteById(expectedId);

        final var aCommand = DeleteDriverByIdCommand.with(expectedId.getValue());

        assertDoesNotThrow(() -> useCase.execute(aCommand));
        verify(driverGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAInvalidCommandWithoutId_whenCallsDeleteDriver_shouldBeOk() {
        final var aCommand = DeleteDriverByIdCommand.with(null);

        assertDoesNotThrow(() -> useCase.execute(aCommand));
        verify(driverGateway, times(0)).deleteById(any());
    }

    @Test
    public void givenAValidCommand_whenCallsDeleteDriverAnGatewayThrowsRandomException_shouldReturnRandomException() {
        final var expectedId = DriverID.from("123");
        final var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage)).when(driverGateway).deleteById(expectedId);

        final var aCommand = DeleteDriverByIdCommand.with(expectedId.getValue());
        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));

        verify(driverGateway, times(1)).deleteById(expectedId);
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
