package com.delivery.system.infrastructure.driver.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<DriverJpaEntity, String> {
}
