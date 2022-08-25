package com.delivery.system.application.order.create;


import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.domain.exceptions.DomainException;
import com.delivery.system.domain.order.OrderGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateOrderUseCaseTest {

    @InjectMocks
    private DefaultCreateOrderUseCase useCase;

    @Mock
    private OrderGateway orderGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateOrder_shouldReturnOrderCreated() {
        final var expectedDescription = "Package";
        final var expectedDriverId = DriverID.unique();

        final var aCommand = CreateOrderCommand.with(expectedDescription, expectedDriverId.getValue());

        when(orderGateway.create(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        useCase.execute(aCommand);

        verify(orderGateway, times(1)).create(any());
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateOrder_shouldReturnDomainException() {
        final var expectedDescription = "  ";
        final var expectedDriverId = DriverID.unique();

        final var expectedErrorMessage = "'description' should not be null";
        final var expectedErrorsCount = 1;

        final var aCommand = CreateOrderCommand.with(expectedDescription, expectedDriverId.getValue());

        final var actualExpcetion = assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(actualExpcetion.getMessage(), expectedErrorMessage);
        assertEquals(actualExpcetion.getErrors().size(), expectedErrorsCount);

        verify(orderGateway, times(0)).create(any());

    }

    @Test
    public void givenAValidCommand_whenCallsCreateOrderAndGatewayThrowsRandonException_shouldReturnARandomException() {
        final var expectedDescription = "Package";
        final var expectedDriverId = DriverID.unique();


        final var expectedErrorMessage = "Gateway error";

        final var aCommand = CreateOrderCommand.with(expectedDescription, expectedDriverId.getValue());

        when(orderGateway.create(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualExpcetion = assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));

        assertEquals(actualExpcetion.getMessage(), expectedErrorMessage);
        verify(orderGateway, times(1)).create(any());

    }

}
