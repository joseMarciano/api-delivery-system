package com.delivery.system.infrastructure.driver;

import com.delivery.system.PostgreGatewayTest;
import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.infrastructure.driver.persistence.DriverJpaEntity;
import com.delivery.system.infrastructure.driver.persistence.DriverRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@PostgreGatewayTest
public class DriverPostgreGatewayTest {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DriverPostreGateway driverPostreGateway;

    @Autowired
    private DriverRepository driverRepository;

    @Test
    public void givenAValidDriver_whenCallsCreateDriver_shouldReturnDriverCreated() {
        final var aDriver = Driver.newDriver("John");

        Assertions.assertEquals(0, driverRepository.count());
        final var actualDriver = driverPostreGateway.create(aDriver);
        Assertions.assertEquals(1, driverRepository.count());

        Assertions.assertEquals(aDriver.getId(), actualDriver.getId());
        Assertions.assertEquals(aDriver.getName(), actualDriver.getName());
        Assertions.assertNotNull(aDriver.getCreatedAt());
        Assertions.assertNotNull(aDriver.getUpdatedAt());
        Assertions.assertEquals(aDriver.getCreatedAt(), actualDriver.getCreatedAt());
        Assertions.assertEquals(aDriver.getUpdatedAt(), actualDriver.getUpdatedAt());

        final var actualEntity =
                driverRepository.findById(actualDriver.getId().getValue()).get();

        Assertions.assertEquals(actualEntity.getId(), actualDriver.getId().getValue());
        Assertions.assertEquals(actualEntity.getName(), actualDriver.getName());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertEquals(actualEntity.getCreatedAt(), actualDriver.getCreatedAt());
        Assertions.assertEquals(actualEntity.getUpdatedAt(), actualDriver.getUpdatedAt());
    }

    @Test
    public void givenAValidPrePersistedDriver_whenCallsUpdateDriver_shouldReturnDriverUpdated() {
        final var aDriver = Driver.newDriver("john");

        final var expectedName = "John";
        final var expectedId = aDriver.getId();

        Assertions.assertEquals(0, driverRepository.count());
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        Assertions.assertEquals(1, driverRepository.count());

        final var aDriverUpdated = aDriver.clone().update(expectedName);

        final var actualDriver = driverPostreGateway
                .update(aDriverUpdated);

        assertEquals(actualDriver.getId(), expectedId);
        assertEquals(actualDriver.getName(), expectedName);
        assertNotNull(actualDriver.getCreatedAt());
        assertNotNull(actualDriver.getUpdatedAt());
        assertTrue(actualDriver.getUpdatedAt().isAfter(aDriver.getCreatedAt()));
    }

    @Test
    public void givenAValidPrePersistedDriver_whenCallsFindById_shouldReturnADriver() {
        final var aDriver = Driver.newDriver("John");
        final var expectedId = aDriver.getId();

        Assertions.assertEquals(0, driverRepository.count());
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        Assertions.assertEquals(1, driverRepository.count());

        final var actualDriver = driverPostreGateway.findById(expectedId).get();

        assertEquals(actualDriver.getName(), aDriver.getName());
        assertEquals(actualDriver.getId(), expectedId);
        assertNotNull(actualDriver.getUpdatedAt());
        assertNotNull(actualDriver.getCreatedAt());
    }

    @Test
    public void givenANoExistingDriver_whenCallsFindById_shouldReturnEmpty() {
        final var expectedId = DriverID.unique();
        Assertions.assertEquals(0, driverRepository.count());
        final var actualOuput = driverPostreGateway.findById(expectedId);
        Assertions.assertTrue(actualOuput.isEmpty());
    }

    @Test
    public void givenAValidPrePersistedDriver_whenCallsDeleteById_shouldBeOk() {
        final var aDriver = Driver.newDriver("John");
        final var expectedId = aDriver.getId();

        Assertions.assertEquals(0, driverRepository.count());
        driverRepository.saveAndFlush(DriverJpaEntity.from(aDriver));
        Assertions.assertEquals(1, driverRepository.count());

        driverPostreGateway.deleteById(expectedId);

        Assertions.assertEquals(0, driverRepository.count());
    }

    @Test
    public void givenANoExistingDriver_whenCallsDeleteById_shouldBeOk() {
        final var expectedId = DriverID.unique();
        Assertions.assertEquals(0, driverRepository.count());
        driverPostreGateway.deleteById(expectedId);
        Assertions.assertEquals(0, driverRepository.count());
    }

    @Test
    public void givenPrePersistedDrivers_whenCallsListAllDriver_shouldReturnAListOfDrivers() {
        final var expectedDrivers =
                List.of(
                        Driver.newDriver("John"),
                        Driver.newDriver("Amanda")
                );

        Assertions.assertEquals(0, driverRepository.count());
        driverRepository.saveAllAndFlush(mapTo(expectedDrivers, DriverJpaEntity::from));
        Assertions.assertEquals(2, driverRepository.count());

        final var actualDrivers = driverPostreGateway.findAll();

        assertTrue(
                actualDrivers.size() == expectedDrivers.size()
                        && expectedDrivers.containsAll(actualDrivers)
        );
    }

    @Test
    public void givenNoExistingDrivers_whenCallsListAllDriver_shouldReturnAEmptyList() {
        final var expectedSizeList = 0;
        Assertions.assertEquals(0, driverRepository.count());
        final var actualDrivers = driverPostreGateway.findAll();
        assertEquals(expectedSizeList, actualDrivers.size());
    }


    private <I, O> List<O> mapTo(List<I> list, Function<I, O> mapper) {
        return list.stream().map(mapper).toList();
    }
}
