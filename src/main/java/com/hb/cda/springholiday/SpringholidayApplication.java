package com.hb.cda.springholiday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringholidayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringholidayApplication.class, args);
    }

}
