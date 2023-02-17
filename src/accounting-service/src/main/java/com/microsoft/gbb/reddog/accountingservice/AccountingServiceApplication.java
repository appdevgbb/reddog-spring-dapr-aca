package com.microsoft.gbb.reddog.accountingservice;

import com.microsoft.applicationinsights.attach.ApplicationInsights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AccountingServiceApplication {

    public static void main(String[] args) {
        ApplicationInsights.attach();
        SpringApplication.run(AccountingServiceApplication.class, args);
    }

}
