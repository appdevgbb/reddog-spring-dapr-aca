package com.microsoft.gbb.reddog.accountingservice;

import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.microsoft.applicationinsights.attach.ApplicationInsights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

// @EnableKafka
@SpringBootApplication
@EnableEurekaClient
@EnableCosmosRepositories
public class AccountingServiceApplication {

    public static void main(String[] args) {
        ApplicationInsights.attach();
        SpringApplication.run(AccountingServiceApplication.class, args);
    }

}
