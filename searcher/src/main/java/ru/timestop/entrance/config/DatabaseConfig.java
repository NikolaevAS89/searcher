package ru.timestop.entrance.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 29.09.2018
 */
@Configuration
@ImportResource({"classpath:db/config/database-entrance.xml"})
public class DatabaseConfig {

}
