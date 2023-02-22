package com.microsoft.gbb.reddog.accountingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.dapr.client.DaprClientBuilder;
import io.dapr.client.DaprPreviewClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DaprConfig {

  private static final DaprClientBuilder BUILDER = new DaprClientBuilder();

  @Bean
  public DaprPreviewClient buildDaprClient() {
    log.info("Creating a new Dapr Client");
    return BUILDER.buildPreviewClient();
  }
}