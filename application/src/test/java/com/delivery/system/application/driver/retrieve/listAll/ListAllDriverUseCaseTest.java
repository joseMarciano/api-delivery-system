package com.delivery.system.application.driver.retrieve.listAll;

import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.driver.DriverGateway;
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
public class ListAllDriverUseCaseTest {

    @InjectMocks
    private DefaultListAllDriverUseCase useCase;

    @Mock
    private DriverGateway driverGateway;

    @Test
    public void givenAValidScenario_whenCallsListAllDriver_shouldReturnAListOfDrivers() {
        final var expectedDrivers =
                List.of(
                        Driver.newDriver("John"),
                        Driver.newDriver("Amanda")
                );

        when(driverGateway.findAll()).thenReturn(expectedDrivers);

        final var actualOuput = useCase.execute();

        assertTrue(
                actualOuput.size() == expectedDrivers.size()
                        && mapTo(expectedDrivers, Driver::getId).containsAll(mapTo(expectedDrivers, Driver::getId))
        );

        verify(driverGateway).findAll();

    }

    @Test
    public void givenAValidScenarioWhereDriversIsEmpty_whenCallsListAllDriver_shouldReturnAEmptyList() {
        final var expectedSizeList = 0;

        when(driverGateway.findAll()).thenReturn(List.of());

        final var actualOuput = useCase.execute();

        assertEquals(expectedSizeList, actualOuput.size());
        verify(driverGateway).findAll();

    }

    @Test
    public void givenAValidScenario_whenCallsListAllDriverAndGatewayThrowsRandomException_shouldReturnRandomException() {
        final var expectedErrorMessage = "Gateway error";

        when(driverGateway.findAll()).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute());

        assertEquals(expectedErrorMessage, actualException.getMessage());
        verify(driverGateway).findAll();

    }

    private <I, O> List<O> mapTo(List<I> list, Function<I, O> mapper) {
        return list.stream().map(mapper).toList();
    }

}
