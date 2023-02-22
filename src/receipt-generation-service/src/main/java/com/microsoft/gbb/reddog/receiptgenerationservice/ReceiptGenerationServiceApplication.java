package com.microsoft.gbb.reddog.receiptgenerationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.microsoft.applicationinsights.attach.ApplicationInsights;

@SpringBootApplication(scanBasePackages =  {"com.microsoft.gbb.reddog.receiptgenerationservice", "io.dapr.springboot" })
public class ReceiptGenerationServiceApplication {

    public static void main(String[] args) {
        ApplicationInsights.attach();
        SpringApplication.run(ReceiptGenerationServiceApplication.class, args);
    }

}