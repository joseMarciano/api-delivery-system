package com.delivery.system;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@SpringBootTest
@ActiveProfiles("e2e-test")
@ExtendWith(value = {PostgreContainerExtension.class, MYSQLGatewayCleanUpExtension.class})
@AutoConfigureMockMvc
public @interface E2ETest {
}
