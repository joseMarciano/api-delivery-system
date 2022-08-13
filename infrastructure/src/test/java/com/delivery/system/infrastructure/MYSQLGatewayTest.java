package com.delivery.system.infrastructure;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

import static org.springframework.context.annotation.ComponentScan.Filter;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@DataJpaTest
@ActiveProfiles("integration-test")
@ComponentScan(
        basePackages = "com.delivery.system",
        includeFilters = @Filter(type = FilterType.REGEX, pattern = "MySQLGateway$")
)
public @interface MYSQLGatewayTest {
}
