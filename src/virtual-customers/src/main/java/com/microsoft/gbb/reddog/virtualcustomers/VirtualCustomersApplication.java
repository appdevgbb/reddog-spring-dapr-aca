package com.microsoft.gbb.reddog.virtualcustomers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.microsoft.applicationinsights.attach.ApplicationInsights;

@SpringBootApplication(scanBasePackages =  {"com.microsoft.gbb.reddog.virtualcustomers", "io.dapr.springboot" })
public class VirtualCustomersApplication {

    public static void main(String[] args) {
        ApplicationInsights.attach();
        SpringApplication.run(VirtualCustomersApplication.class, args);
    }

}
