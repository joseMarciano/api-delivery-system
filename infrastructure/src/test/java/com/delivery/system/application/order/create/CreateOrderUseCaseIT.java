package com.delivery.system.application.order.create;

import com.delivery.system.DisableRabbitMQ;
import com.delivery.system.IntegrationTest;
import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.exceptions.DomainException;
import com.delivery.system.domain.order.OrderGateway;
import com.delivery.system.infrastructure.driver.persistence.DriverJpaEntity;
import com.delivery.system.infrastructure.driver.persistence.DriverRepository;
import com.delivery.system.infrastructure.order.persistence.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisableRabbitMQ
public class CreateOrderUseCaseIT {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DefaultCreateOrderUseCase useCase;

    @Autowired
    private OrderGateway orderGateway;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DriverRepository driverRepository;


    @Test
    public void givenAValidCommand_whenCallsCreateOrder_shouldReturnOrderCreated() {
        assertEquals(0, orderRepository.count());
        assertEquals(0, driverRepository.count());

        final var expectedDescription = "Package 1";
        final var expectedDriver = Driver.newDriver("John");

        driverRepository.saveAndFlush(DriverJpaEntity.from(expectedDriver));

        final var aCommand = CreateOrderCommand.with(expectedDescription, expectedDriver.getId().getValue());

        useCase.execute(aCommand);

        assertEquals(1, orderRepository.count());
        final var actualEntity = orderRepository.findAll().get(0);

        assertNotNull(actualEntity.getId());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertEquals(expectedDriver, actualEntity.getDriver().toAggregate());
        assertNotNull(actualEntity.getCreatedAt());
        assertNotNull(actualEntity.getUpdatedAt());
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateOrder_shouldReturnDomainException() {
        assertEquals(0, orderRepository.count());
        assertEquals(0, driverRepository.count());

        final var expectedDescription = "  ";
        final var expectedErrorMessage = "'description' should not be null";
        final var expectedErrorsCount = 1;

        final var aDriver = Driver.newDriver("John");
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        assertEquals(1, driverRepository.count());

        final var aCommand =
                CreateOrderCommand.with(expectedDescription, aDriver.getId().getValue());

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(actualException.getMessage(), expectedErrorMessage);
        assertEquals(actualException.getErrors().size(), expectedErrorsCount);
        assertEquals(0, orderRepository.count());
    }
}
