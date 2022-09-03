package com.delivery.system;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@EnableAutoConfiguration(exclude = {RabbitAutoConfiguration.class})
public @interface DisableRabbitMQ {
}
