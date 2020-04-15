package com.jackie.replace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jackie.*"})
public class ReplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReplaceApplication.class, args);
    }

}
