package com.microsoft.gbb.reddog.loyaltyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.microsoft.applicationinsights.attach.ApplicationInsights;

@SpringBootApplication
@EnableFeignClients
public class LoyaltyServiceApplication {

    public static void main(String[] args) {
        ApplicationInsights.attach();
        SpringApplication.run(LoyaltyServiceApplication.class, args);
    }

}
