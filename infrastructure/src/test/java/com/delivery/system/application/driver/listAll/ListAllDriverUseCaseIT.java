package com.delivery.system.application.driver.listAll;

import com.delivery.system.DisableRabbitMQ;
import com.delivery.system.IntegrationTest;
import com.delivery.system.application.driver.retrieve.listAll.ListAllDriverOutput;
import com.delivery.system.application.driver.retrieve.listAll.ListAllDriverUseCase;
import com.delivery.system.domain.driver.Driver;
import com.delivery.system.infrastructure.driver.persistence.DriverJpaEntity;
import com.delivery.system.infrastructure.driver.persistence.DriverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
@DisableRabbitMQ
public class ListAllDriverUseCaseIT {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ListAllDriverUseCase useCase;

    @Autowired
    private DriverRepository driverRepository;

    @Test
    public void givenAValidScenario_whenCallsListAllDriver_shouldReturnAListOfDrivers() {
        final var expectedDrivers =
                List.of(
                        Driver.newDriver("John"),
                        Driver.newDriver("Amanda")
                );

        assertEquals(0, driverRepository.count());
        driverRepository.saveAllAndFlush(mapTo(expectedDrivers, DriverJpaEntity::from));
        assertEquals(2, driverRepository.count());

        final var actualOuput = useCase.execute();

        assertTrue(
                actualOuput.size() == expectedDrivers.size()
                        && mapTo(expectedDrivers, ListAllDriverOutput::from).containsAll(actualOuput)
        );
    }

    @Test
    public void givenAValidScenarioWhereDriversIsEmpty_whenCallsListAllDriver_shouldReturnAEmptyList() {
        final var expectedSizeList = 0;

        assertEquals(0, driverRepository.count());
        final var actualOuput = useCase.execute();
        assertEquals(0, driverRepository.count());

        assertEquals(expectedSizeList, actualOuput.size());
    }

    private <I, O> List<O> mapTo(List<I> list, Function<I, O> mapper) {
        return list.stream().map(mapper).toList();
    }

}
