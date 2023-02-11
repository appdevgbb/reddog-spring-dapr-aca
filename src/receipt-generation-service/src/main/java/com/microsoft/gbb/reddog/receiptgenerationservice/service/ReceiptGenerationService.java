package com.microsoft.gbb.reddog.receiptgenerationservice.service;

import com.microsoft.gbb.reddog.receiptgenerationservice.dto.OrderSummaryDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;

/**
 * Receipt generation service
 */
@Slf4j
@Component
public class ReceiptGenerationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptGenerationService.class);
    private final DaprClient client = (new DaprClientBuilder()).build();

    public void generateReceipt(OrderSummaryDto orderSummary) {
        LOGGER.info("Generating receipt");
        client.invokeBinding("reddog.binding.receipt", "create", orderSummary).block();
    }
}
