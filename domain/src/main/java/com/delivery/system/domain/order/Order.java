package com.delivery.system.domain.order;

import com.delivery.system.domain.AggregateRoot;
import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.domain.validation.ValidationHandler;
import com.delivery.system.domain.validation.handler.ThrowsValidationHandler;
import com.delivery.system.util.InstantUtils;

import java.time.Instant;

public class Order extends AggregateRoot<OrderID> implements Cloneable {

    private String description;

    private DriverID driverID;

    private StatusOrder status;

    private Instant timeDelivered;
    private final Instant createdAt;
    private Instant updatedAt;

    public static Order newOrder(final String aDescription, final DriverID anDriverId) {
        final var anId = OrderID.unique();
        final var now = InstantUtils.now();
        return new Order(anId, aDescription, anDriverId, null, now, now, StatusOrder.CREATED);
    }

    private Order(final OrderID anId,
                  final String aDescription,
                  final DriverID aDriverId,
                  final Instant deliveredAt,
                  final Instant createdAt,
                  final Instant updatedAt,
                  final StatusOrder aStatus) {
        super(anId);
        this.description = aDescription;
        this.status = aStatus;
        this.driverID = aDriverId;
        this.timeDelivered = deliveredAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        selfValidate();
    }

    public static Order with(final DriverID aDriverId,
                             final OrderID anId,
                             final String aDescription,
                             final StatusOrder aStatus,
                             final Instant deliveredAt,
                             final Instant createdAt,
                             final Instant updatedAt) {
        return new Order(
                anId,
                aDescription,
                aDriverId,
                deliveredAt,
                createdAt,
                updatedAt,
                aStatus
        );
    }

    @Override
    public void validate(ValidationHandler anHandler) {
        new OrderValidator(this, anHandler).validate();
    }

    public Order update(final String aDescription,
                        final DriverID aDriverId) {

        this.description = aDescription;
        this.driverID = aDriverId;
        this.updatedAt = InstantUtils.now();

        selfValidate();
        return this;
    }

    public Order changeStatus(final StatusOrder newStatus) {
        this.status = newStatus;
        this.timeDelivered = this.status == StatusOrder.DELIVERED ? InstantUtils.now() : null;
        this.updatedAt = InstantUtils.now();

        selfValidate();
        return this;
    }

    @Override
    public Order clone() {
        try {
            return (Order) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public String getDescription() {
        return description;
    }

    public DriverID getDriverID() {
        return driverID;
    }

    public StatusOrder getStatus() {
        return status;
    }

    public Instant getTimeDelivered() {
        return timeDelivered;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private void selfValidate() {
        validate(new ThrowsValidationHandler());
    }
}
