package com.delivery.sysmtem.domain.order;


import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.domain.exceptions.DomainException;
import com.delivery.system.domain.order.Order;
import com.delivery.system.domain.order.StatusOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    public void givenAValidParams_whenCallNewOrder_thenInstanciateAOrder() {
        final var expectedDescription = "Package";
        final var expectedDriverID = DriverID.unique();

        final var actualOrder = Order.newOrder(expectedDescription, expectedDriverID);

        assertEquals(expectedDescription, actualOrder.getDescription());
        assertEquals(expectedDriverID, actualOrder.getDriverID());
        assertEquals(StatusOrder.CREATED, actualOrder.getStatus());
        assertNotNull(actualOrder.getCreatedAt());
        assertNotNull(actualOrder.getUpdatedAt());
        assertNull(actualOrder.getTimeDelivered());
    }

    @Test
    public void givenAEmptyDescription_whenCallNewOrder_thenThrowsDomainException() {
        final var expectedDescription = " ";
        final var expectedDriverID = DriverID.unique();

        final var expectedErrorMessage = "'description' should not be null";

        final var actualException =
                assertThrows(DomainException.class, () -> Order.newOrder(expectedDescription, expectedDriverID));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenANullName_whenCallNewOrder_thenThrowsDomainException() {
        final String expectedDescription = null;
        final var expectedDriverID = DriverID.unique();

        final var expectedErrorMessage = "'description' should not be null";

        final var actualException =
                assertThrows(DomainException.class, () -> Order.newOrder(expectedDescription, expectedDriverID));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenANullDriverID_whenCallNewOrder_thenThrowsDomainException() {
        final var expectedDescription = "Package";
        final DriverID expectedDriverID = null;

        final var expectedErrorMessage = "'driverId' should not be null";

        final var actualException =
                assertThrows(DomainException.class, () -> Order.newOrder(expectedDescription, expectedDriverID));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidParam_whenCallsUpdate_thenReturnOrderUpdated() {
        final var expectedDescription = "Package";
        final var expectedDriverId = DriverID.unique();

        final var aOrder = Order.newOrder("pacage", expectedDriverId);

        final var actualOrder = aOrder.update(expectedDescription, aOrder.getDriverID());

        assertEquals(expectedDescription, actualOrder.getDescription());
        assertEquals(StatusOrder.CREATED, actualOrder.getStatus());
        assertNotNull(actualOrder.getCreatedAt());
        assertNotNull(actualOrder.getUpdatedAt());
        assertTrue(actualOrder.getCreatedAt().isBefore(actualOrder.getUpdatedAt()));
        assertNull(actualOrder.getTimeDelivered());
    }

    @Test
    public void givenANullName_whenCallUpdate_thenThrowsDomainException() {
        final String expectedDescription = null;
        final var expectedDriverId = DriverID.unique();

        final var expectedErrorMessage = "'description' should not be null";
        final var aOrder = Order.newOrder("pacage", expectedDriverId);

        final var actualException =
                assertThrows(DomainException.class, () -> aOrder.update(expectedDescription, expectedDriverId));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenANullDriverID_whenCallUpdate_thenThrowsDomainException() {
        final var expectedDescription = "Package";
        final DriverID expectedDriverId = null;

        final var expectedErrorMessage = "'driverId' should not be null";
        final var aOrder = Order.newOrder(expectedDescription, DriverID.unique());

        final var actualException =
                assertThrows(DomainException.class, () -> aOrder.update(expectedDescription, expectedDriverId));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidParam_whenCallsChangeStatusInProgress_thenReturnOrderStatusChanged() {
        final var expectedDescription = "Package";
        final var expectedDriverId = DriverID.unique();
        final var expectedStatus = StatusOrder.IN_PROGRESS;

        final var aOrder = Order.newOrder(expectedDescription, expectedDriverId);

        final var actualOrder = aOrder.changeStatus(expectedStatus);

        assertEquals(expectedDescription, actualOrder.getDescription());
        assertEquals(expectedStatus, actualOrder.getStatus());
        assertNotNull(actualOrder.getCreatedAt());
        assertNotNull(actualOrder.getUpdatedAt());
        assertTrue(actualOrder.getCreatedAt().isBefore(actualOrder.getUpdatedAt()));
        assertNull(actualOrder.getTimeDelivered());
    }

    @Test
    public void givenAValidParam_whenCallsChangeStatusDelivered_thenReturnOrderStatusChanged() {
        final var expectedDescription = "Package";
        final var expectedDriverId = DriverID.unique();
        final var expectedStatus = StatusOrder.DELIVERED;

        final var aOrder = Order.newOrder(expectedDescription, expectedDriverId);

        final var actualOrder = aOrder.changeStatus(expectedStatus);

        assertEquals(expectedDescription, actualOrder.getDescription());
        assertEquals(expectedStatus, actualOrder.getStatus());
        assertNotNull(actualOrder.getCreatedAt());
        assertNotNull(actualOrder.getUpdatedAt());
        assertTrue(actualOrder.getCreatedAt().isBefore(actualOrder.getUpdatedAt()));
        assertNotNull(actualOrder.getTimeDelivered());
    }

    @Test
    public void givenAInvalidParam_whenCallsChangeStatusWithNullValue_shouldReturnDomainException() {
        final var expectedDescription = "Package";
        final var expectedDriverId = DriverID.unique();
        final StatusOrder expectedStatus = null;

        final var expectedErrorMessage = "'statusOrder' should not be null";

        final var aOrder = Order.newOrder(expectedDescription, expectedDriverId);

        final var actualException =
                assertThrows(DomainException.class, () -> aOrder.changeStatus(expectedStatus));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}
