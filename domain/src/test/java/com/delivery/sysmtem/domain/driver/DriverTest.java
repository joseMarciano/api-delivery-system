package com.delivery.sysmtem.domain.driver;


import com.delivery.system.domain.driver.Driver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DriverTest {

    @Test
    public void givenAValidParams_whenCallNewDriver_thenInstanciateADriver(){
        final var expectedName = "John";
        final var expectedCpf = "02254799978";

        final var actualDriver = Driver.newDriver(expectedName, expectedCpf);

        Assertions.assertEquals(expectedName, actualDriver.getName());
        Assertions.assertEquals(expectedCpf, actualDriver.getCpf());
        Assertions.assertNotNull(actualDriver.getCreatedAt());
        Assertions.assertNotNull(actualDriver.getUpdatedAt());
    }

}
