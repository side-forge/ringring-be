package com.sideforge.ringring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RingringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RingringApplication.class, args);
    }

}
