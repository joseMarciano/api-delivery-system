package com.delivery.system.infrastructure.driver.persistence;


import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.driver.DriverID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity(name = "Driver")
@Table(name = "DRIVERS")
public class DriverJpaEntity {

    @Id
    @Column(name = "ID", updatable = false)
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CREATED_AT", updatable = false)
    private Instant createdAt;

    @Column(name = "UPDATED_AT")
    private Instant updatedAt;

    public DriverJpaEntity() {
    }

    private DriverJpaEntity(String id, String name, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DriverJpaEntity from(final Driver aDriver) {
        return new DriverJpaEntity(
                aDriver.getId().getValue(),
                aDriver.getName(),
                aDriver.getCreatedAt(),
                aDriver.getUpdatedAt()
        );
    }

    public static DriverJpaEntity with(final DriverID aDriverId) {
        return new DriverJpaEntity(aDriverId.getValue(), null, null, null);
    }

    public Driver toAggregate() {
        return Driver.with(
                DriverID.from(this.getId()),
                this.getName(),
                this.getCreatedAt(),
                this.getUpdatedAt()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
