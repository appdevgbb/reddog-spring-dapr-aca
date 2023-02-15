package com.microsoft.gbb.reddog.makelineservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.microsoft.applicationinsights.attach.ApplicationInsights;

@SpringBootApplication
public class MakelineServiceApplication {

    public static void main(String[] args) {
        ApplicationInsights.attach();
        SpringApplication.run(MakelineServiceApplication.class, args);
    }

}
