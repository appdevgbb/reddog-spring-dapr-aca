package com.microsoft.gbb.reddog.accountingservice;

import com.microsoft.applicationinsights.attach.ApplicationInsights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages =  {"com.microsoft.gbb.reddog.accountingservice", "io.dapr.springboot" })
public class AccountingServiceApplication {

    public static void main(String[] args) {
        ApplicationInsights.attach();
        SpringApplication.run(AccountingServiceApplication.class, args);
    }
}