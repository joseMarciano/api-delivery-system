package com.delivery.system.application.order.create;


import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.domain.exceptions.DomainException;
import com.delivery.system.domain.order.Order;
import com.delivery.system.domain.order.OrderID;
import com.delivery.system.domain.order.OrderNotifier;
import com.delivery.system.domain.validation.Error;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateOrderNotifierUseCaseTest {

    @InjectMocks
    private DefaultCreateOrderNotifierUseCase useCase;

    @Mock
    private OrderNotifier notifier;

    @Mock
    private CreateOrderUseCase orderUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateOrder_shouldNotifyOrderCreated() {
        final var expectedDescription = "Package";
        final var expectedDriverId = DriverID.unique();
        final var expectedOutput =
                CreateOrderOuput.from(Order.newOrder(expectedDescription, expectedDriverId));


        final var aCommand = CreateOrderCommand.with(expectedDescription, expectedDriverId.getValue());

        when(orderUseCase.execute(aCommand)).thenReturn(expectedOutput);

        useCase.execute(aCommand);

        verify(notifier, times(1)).notifyCreated(OrderID.from(expectedOutput.id()));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateOrder_shouldReturnDomainException() {
        final var expectedDescription = "  ";
        final var expectedDriverId = DriverID.unique();

        final var expectedErrorMessage = "'description' should not be null";
        final var expectedErrorsCount = 1;


        final var aCommand = CreateOrderCommand.with(expectedDescription, expectedDriverId.getValue());
        when(orderUseCase.execute(aCommand)).thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        final var actualExpcetion = assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(actualExpcetion.getMessage(), expectedErrorMessage);
        assertEquals(actualExpcetion.getErrors().size(), expectedErrorsCount);

        verify(notifier, times(0)).notifyCreated(any());

    }

    @Test
    public void givenAValidCommand_whenCallsCreateOrderAndGatewayThrowsRandonException_shouldReturnARandomException() {
        final var expectedDescription = "Package";
        final var expectedDriverId = DriverID.unique();
        final var expectedOutput =
                CreateOrderOuput.from(Order.newOrder(expectedDescription, expectedDriverId));


        final var expectedErrorMessage = "Gateway error";

        final var aCommand = CreateOrderCommand.with(expectedDescription, expectedDriverId.getValue());
        when(orderUseCase.execute(aCommand)).thenReturn(expectedOutput);
        when(notifier.notifyCreated(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualExpcetion = assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));

        assertEquals(actualExpcetion.getMessage(), expectedErrorMessage);
        verify(notifier, times(1)).notifyCreated(any());

    }

}
