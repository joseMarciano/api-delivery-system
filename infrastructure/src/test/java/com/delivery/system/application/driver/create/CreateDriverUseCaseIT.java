package com.delivery.system.application.driver.create;


import com.delivery.system.DisableRabbitMQ;
import com.delivery.system.IntegrationTest;
import com.delivery.system.domain.exceptions.DomainException;
import com.delivery.system.infrastructure.driver.persistence.DriverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisableRabbitMQ //TODO: FIND BETTER WAY TO DISABLE RABBITMQ WHERE DOEST NEED WITHOUT MOCKBEAN
public class CreateDriverUseCaseIT {

    @Autowired
    private CreateDriverUseCase useCase;

    @Autowired
    private DriverRepository driverRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    public void givenAValidCommand_whenCallsCreateDriver_shouldReturnDriverCreated() {
        final var expectedName = "John";

        assertEquals(0, driverRepository.count());
        final var aCommand = CreateDriverCommand.with(expectedName);
        final var actualOutput = useCase.execute(aCommand);

        assertEquals(actualOutput.name(), expectedName);
        assertNotNull(actualOutput.id());
        assertNotNull(actualOutput.createdAt());
        assertNotNull(actualOutput.updatedAt());


        assertEquals(1, driverRepository.count());
        final var actualEntity = driverRepository.findById(actualOutput.id()).get();

        assertEquals(actualOutput.name(), actualEntity.getName());
        assertEquals(actualOutput.createdAt(), actualEntity.getCreatedAt());
        assertEquals(actualOutput.updatedAt(), actualEntity.getUpdatedAt());
        assertEquals(actualOutput.id(), actualEntity.getId());
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateDriver_shouldReturnDomainException() {
        final var expectedName = "  ";
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorsCount = 1;

        assertEquals(0, driverRepository.count());

        final var aCommand = CreateDriverCommand.with(expectedName);
        final var actualExpcetion = assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(actualExpcetion.getMessage(), expectedErrorMessage);
        assertEquals(actualExpcetion.getErrors().size(), expectedErrorsCount);
        assertEquals(0, driverRepository.count());

    }

}
