package com.delivery.system.infrastructure.order.persistence;

import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.domain.order.Order;
import com.delivery.system.domain.order.OrderID;
import com.delivery.system.domain.order.StatusOrder;
import com.delivery.system.infrastructure.driver.persistence.DriverJpaEntity;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "order")
@Table(name = "ORDERS")
public class OrderJpaEntity {

    @Id
    @Column(name = "ID", updatable = false)
    private String id;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "ID_DRIVERS", referencedColumnName = "ID")
    private DriverJpaEntity driver;

    @Column(name = "STATUS")
    private StatusOrder status;

    @Column(name = "DELIVERED_AT")
    private Instant deliveredAt;

    @Column(name = "CREATED_AT", updatable = false)
    private Instant createdAt;

    @Column(name = "UPDATED_AT")
    private Instant updatedAt;

    public OrderJpaEntity() {
    }

    public static OrderJpaEntity from(final Order aOrder) {
        return new OrderJpaEntity(
                DriverJpaEntity.with(aOrder.getDriverID()),
                aOrder.getId().getValue(),
                aOrder.getDescription(),
                aOrder.getStatus(),
                aOrder.getTimeDelivered(),
                aOrder.getCreatedAt(),
                aOrder.getUpdatedAt()
        );
    }

    public Order toAggregate() {
        return Order.with(
                DriverID.from(this.getDriver().getId()),
                OrderID.from(this.getId()),
                this.getDescription(),
                this.getStatus(),
                this.getDeliveredAt(),
                this.getCreatedAt(),
                this.getUpdatedAt()
        );
    }

    private OrderJpaEntity(final DriverJpaEntity aDriverEntity,
                           final String id,
                           final String description,
                           final StatusOrder status,
                           final Instant deliveredAt,
                           final Instant createdAt,
                           final Instant updatedAt) {

        this.driver = aDriverEntity;
        this.id = id;
        this.description = description;
        this.status = status;
        this.deliveredAt = deliveredAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusOrder getStatus() {
        return status;
    }

    public void setStatus(StatusOrder status) {
        this.status = status;
    }

    public Instant getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(Instant deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DriverJpaEntity getDriver() {
        return driver;
    }

    public void setDriver(DriverJpaEntity driver) {
        this.driver = driver;
    }
}
