package com.openquartz.logserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {DataSourceTransactionManagerAutoConfiguration.class})
@EnableTransactionManagement(order = 0)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
