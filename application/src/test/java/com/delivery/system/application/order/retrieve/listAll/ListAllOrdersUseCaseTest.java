package com.delivery.system.application.order.retrieve.listAll;

import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.domain.order.Order;
import com.delivery.system.domain.order.OrderGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListAllOrdersUseCaseTest {

    @InjectMocks
    private DefaultListAllOrdersUseCase useCase;

    @Mock
    private OrderGateway orderGateway;

    @Test
    public void givenAValidScenario_whenCallsListAllOrders_shouldReturnAListOfOrders() {
        final var expectedOrders =
                List.of(
                        Order.newOrder("Package 1", DriverID.unique()),
                        Order.newOrder("Package 2", DriverID.unique())
                );

        when(orderGateway.findAll()).thenReturn(expectedOrders);

        final var actualOuput = useCase.execute();

        assertTrue(
                actualOuput.size() == expectedOrders.size()
                        && mapTo(expectedOrders, Order::getId).containsAll(mapTo(expectedOrders, Order::getId))
        );

        verify(orderGateway).findAll();

    }

    @Test
    public void givenAValidScenarioWhereOrdersIsEmpty_whenCallsListAllOrders_shouldReturnAEmptyList() {
        final var expectedSizeList = 0;

        when(orderGateway.findAll()).thenReturn(List.of());

        final var actualOuput = useCase.execute();

        assertEquals(expectedSizeList, actualOuput.size());
        verify(orderGateway).findAll();

    }

    @Test
    public void givenAValidScenario_whenCallsListAllOrdersAndGatewayThrowsRandomException_shouldReturnRandomException() {
        final var expectedErrorMessage = "Gateway error";

        when(orderGateway.findAll()).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute());

        assertEquals(expectedErrorMessage, actualException.getMessage());
        verify(orderGateway).findAll();

    }

    private <I, O> List<O> mapTo(List<I> list, Function<I, O> mapper) {
        return list.stream().map(mapper).toList();
    }

}
