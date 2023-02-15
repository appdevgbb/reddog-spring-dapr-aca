package com.microsoft.gbb.reddog.virtualworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.microsoft.applicationinsights.attach.ApplicationInsights;

@SpringBootApplication
public class VirtualWorkerApplication {

    public static void main(String[] args) {
        ApplicationInsights.attach();
        SpringApplication.run(VirtualWorkerApplication.class, args);
    }

}
