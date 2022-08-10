package com.delivery.sysmtem.domain.driver;


import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DriverTest {

    @Test
    public void givenAValidParams_whenCallNewDriver_thenInstanciateADriver() {
        final var expectedName = "John";

        final var actualDriver = Driver.newDriver(expectedName);

        assertEquals(expectedName, actualDriver.getName());
        assertNotNull(actualDriver.getCreatedAt());
        assertNotNull(actualDriver.getUpdatedAt());
    }

    @Test
    public void givenAEmptyName_whenCallNewDriver_thenThrowsDomainException() {
        final var expectedName = " ";
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException =
                assertThrows(DomainException.class, () -> Driver.newDriver(expectedName));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenANullName_whenCallNewDriver_thenThrowsDomainException() {
        final String expectedName = null;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException =
                assertThrows(DomainException.class, () -> Driver.newDriver(expectedName));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidParam_whenCallsUpdate_thenReturnDriverUpdated() {
        final String expectedName = "John";
        final var aDriver = Driver.newDriver("john");

        final var actualDriver = aDriver.update(expectedName);

        assertEquals(expectedName, actualDriver.getName());
        assertNotNull(actualDriver.getCreatedAt());
        assertNotNull(actualDriver.getUpdatedAt());
        assertTrue(actualDriver.getCreatedAt().isBefore(actualDriver.getUpdatedAt()));
    }

    @Test
    public void givenANullName_whenCallUpdate_thenThrowsDomainException() {
        final String expectedName = null;
        final var expectedErrorMessage = "'name' should not be null";
        final var aDriver = Driver.newDriver("john");

        final var actualException =
                assertThrows(DomainException.class, () -> aDriver.update(expectedName));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
