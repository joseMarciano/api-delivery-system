package com.delivery.system;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("e2e-test")
@ExtendWith(MYSQLGatewayCleanUpExtension.class)
public @interface E2ETest {

}
