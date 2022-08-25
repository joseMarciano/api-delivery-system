package com.delivery.system.application.order.updateStatus;

import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.domain.exceptions.DomainException;
import com.delivery.system.domain.order.Order;
import com.delivery.system.domain.order.OrderGateway;
import com.delivery.system.domain.order.OrderID;
import com.delivery.system.domain.order.StatusOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateStatusOrderUseCaseTest {

    @InjectMocks
    private DefaultUpdateStatusOrderUseCase useCase;

    @Mock
    private OrderGateway orderGateway;

    @Test
    public void givenAValidParam_whenCallsChangeStatusInProgress_thenReturnOrderStatusChanged() {
        final var expectedDescription = "Package";
        final var expectedDriverId = DriverID.unique();
        final var expectedStatus = StatusOrder.IN_PROGRESS;
        final var aOrder = Order.newOrder(expectedDescription, expectedDriverId);
        final var expectedId = aOrder.getId();

        when(orderGateway.findById(any(OrderID.class))).thenReturn(Optional.of(aOrder));

        final var aCommand = UpdateStatusOrderCommand.with(expectedId.getValue(), expectedStatus);

        useCase.execute(aCommand);

        verify(orderGateway).findById(any(OrderID.class));
        verify(orderGateway).update(argThat(order ->
                order.getDescription().equals(expectedDescription)
                        && order.getDriverID().equals(expectedDriverId)
                        && order.getStatus().equals(expectedStatus)
                        && nonNull(order.getCreatedAt())
                        && nonNull(order.getUpdatedAt())
                        && order.getCreatedAt().isBefore(order.getUpdatedAt())
                        && isNull(order.getTimeDelivered())
        ));
    }

    @Test
    public void givenAValidParam_whenCallsChangeStatusDelivered_thenReturnOrderStatusChanged() {
        final var expectedDescription = "Package";
        final var expectedDriverId = DriverID.unique();
        final var expectedStatus = StatusOrder.DELIVERED;
        final var aOrder = Order.newOrder(expectedDescription, expectedDriverId);
        final var expectedId = aOrder.getId();

        when(orderGateway.findById(any(OrderID.class))).thenReturn(Optional.of(aOrder));

        final var aCommand = UpdateStatusOrderCommand.with(expectedId.getValue(), expectedStatus);

        useCase.execute(aCommand);

        verify(orderGateway).findById(any(OrderID.class));
        verify(orderGateway).update(argThat(order ->
                order.getDescription().equals(expectedDescription)
                        && order.getDriverID().equals(expectedDriverId)
                        && order.getStatus().equals(expectedStatus)
                        && nonNull(order.getCreatedAt())
                        && nonNull(order.getUpdatedAt())
                        && order.getCreatedAt().isBefore(order.getUpdatedAt())
                        && nonNull(order.getTimeDelivered())
        ));
    }

    @Test
    public void givenAInvalidParam_whenCallsChangeStatusWithNullValue_shouldReturnDomainException() {
        final var expectedDescription = "Package";
        final var expectedDriverId = DriverID.unique();
        final StatusOrder expectedStatus = null;
        final var aOrder = Order.newOrder(expectedDescription, expectedDriverId);
        final var expectedId = aOrder.getId();

        final var expectedErrorMessage = "'statusOrder' should not be null";

        when(orderGateway.findById(any(OrderID.class))).thenReturn(Optional.of(aOrder));

        final var aCommand =
                UpdateStatusOrderCommand.with(expectedId.getValue(), expectedStatus);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        verify(orderGateway).findById(any(OrderID.class));
        verify(orderGateway, times(0)).update(any());

        Assertions.assertEquals(actualException.getMessage(), expectedErrorMessage);
    }
}
