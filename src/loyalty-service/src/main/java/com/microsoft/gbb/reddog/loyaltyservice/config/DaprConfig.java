package com.microsoft.gbb.reddog.loyaltyservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.dapr.client.DaprClientBuilder;
import io.dapr.client.DaprClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DaprConfig {

  private static final DaprClientBuilder BUILDER = new DaprClientBuilder();

  @Bean
  public DaprClient buildDaprClient() {
    log.info("Creating a new Dapr Client");
    return BUILDER.build();
  }
}