package com.delivery.system.application.driver.create;


import com.delivery.system.domain.driver.DriverGateway;
import com.delivery.system.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateDriverUseCaseTest {

    @InjectMocks
    private DefaultCreateDriverUseCase useCase;

    @Mock
    private DriverGateway driverGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateDriver_shouldReturnDriverCreated() {
        final var expectedName = "John";

        final var aCommand = CreateDriverCommand.with(expectedName);

        when(driverGateway.create(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(actualOutput.name(), expectedName);
        assertNotNull(actualOutput.id());
        assertNotNull(actualOutput.createdAt());
        assertNotNull(actualOutput.updatedAt());

    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateDriver_shouldReturnDomainException() {
        final var expectedName = "  ";
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorsCount = 1;

        final var aCommand = CreateDriverCommand.with(expectedName);

        final var actualExpcetion = assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(actualExpcetion.getMessage(), expectedErrorMessage);
        assertEquals(actualExpcetion.getErrors().size(), expectedErrorsCount);

        verify(driverGateway, times(0)).create(any());

    }

    @Test
    public void givenAValidCommand_whenCallsCreateDriverAndGatewayThrowsRandonException_shouldReturnARandomException() {
        final var expectedName = "John";
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = CreateDriverCommand.with(expectedName);

        when(driverGateway.create(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualExpcetion = assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));

        assertEquals(actualExpcetion.getMessage(), expectedErrorMessage);
        verify(driverGateway, times(1)).create(any());

    }

}
