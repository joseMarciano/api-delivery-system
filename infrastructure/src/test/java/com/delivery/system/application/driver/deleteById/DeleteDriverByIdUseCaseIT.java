package com.delivery.system.application.driver.deleteById;

import com.delivery.system.DisableRabbitMQ;
import com.delivery.system.IntegrationTest;
import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.infrastructure.driver.persistence.DriverJpaEntity;
import com.delivery.system.infrastructure.driver.persistence.DriverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
@DisableRabbitMQ
public class DeleteDriverByIdUseCaseIT {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DeleteDriverByIdUseCase useCase;

    @Autowired
    private DriverRepository driverRepository;


    @Test
    public void givenAValidCommand_whenCallsDeleteDriver_shouldBeOk() {
        final var expectedName = "John";
        final var aDriver = Driver.newDriver(expectedName);
        final var expectedId = aDriver.getId();

        assertEquals(0, driverRepository.count());
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        assertEquals(1, driverRepository.count());

        final var aCommand = DeleteDriverByIdCommand.with(expectedId.getValue());

        useCase.execute(aCommand);
        assertEquals(0, driverRepository.count());
    }

    @Test
    public void givenAValidCommandWithNoExistingDriver_whenCallsDeleteDriver_shouldBeOk() {
        final var expectedId = DriverID.from("not-existing-id");

        assertEquals(0, driverRepository.count());

        final var aCommand = DeleteDriverByIdCommand.with(expectedId.getValue());

        useCase.execute(aCommand);
        assertEquals(0, driverRepository.count());

    }

    @Test
    public void givenAInvalidCommandWithoutId_whenCallsDeleteDriver_shouldBeOk() {
        assertEquals(0, driverRepository.count());

        final var aCommand = DeleteDriverByIdCommand.with(null);

        assertDoesNotThrow(() -> useCase.execute(aCommand));
        assertEquals(0, driverRepository.count());
    }
}
