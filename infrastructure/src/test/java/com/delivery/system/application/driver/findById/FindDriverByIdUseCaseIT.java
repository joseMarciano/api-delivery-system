package com.delivery.system.application.driver.findById;

import com.delivery.system.DisableRabbitMQ;
import com.delivery.system.IntegrationTest;
import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.domain.exceptions.NotFoundException;
import com.delivery.system.infrastructure.driver.persistence.DriverJpaEntity;
import com.delivery.system.infrastructure.driver.persistence.DriverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisableRabbitMQ
public class FindDriverByIdUseCaseIT {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FindDriverByIdUseCase useCase;

    @Autowired
    private DriverRepository driverRepository;


    @Test
    public void givenAValidCommand_whenCallsFindById_shouldReturnADriver() {
        final var expectedName = "John";
        final var aDriver = Driver.newDriver(expectedName);
        final var expectedId = aDriver.getId();

        assertEquals(0, driverRepository.count());
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        assertEquals(1, driverRepository.count());

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

        assertEquals(0, driverRepository.count());
        final var aCommand = FindDriverByIdCommand.with(expectedId.getValue());

        final var actualOuput = assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        assertEquals(actualOuput.getMessage(), expectedErrorMessage);
        assertEquals(0, driverRepository.count());
    }
}
