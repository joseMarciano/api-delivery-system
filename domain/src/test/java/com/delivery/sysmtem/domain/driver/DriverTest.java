package com.delivery.sysmtem.domain.driver;


import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DriverTest {

    @Test
    public void givenAValidParams_whenCallNewDriver_thenInstanciateADriver() {
        final var expectedName = "John";
        final var expectedCpf = "02254799978";

        final var actualDriver = Driver.newDriver(expectedName, expectedCpf);

        assertEquals(expectedName, actualDriver.getName());
        assertEquals(expectedCpf, actualDriver.getCpf());
        assertNotNull(actualDriver.getCreatedAt());
        assertNotNull(actualDriver.getUpdatedAt());
    }

    @Test
    public void givenAEmptyName_whenCallNewDriver_thenThrowsDomainException() {
        final var expectedName = " ";
        final var expectedCpf = "02254799978";
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException =
                assertThrows(DomainException.class, () -> Driver.newDriver(expectedName, expectedCpf));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenANullName_whenCallNewDriver_thenThrowsDomainException() {
        final String expectedName = null;
        final var expectedCpf = "02254799978";
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException =
                assertThrows(DomainException.class, () -> Driver.newDriver(expectedName, expectedCpf));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
