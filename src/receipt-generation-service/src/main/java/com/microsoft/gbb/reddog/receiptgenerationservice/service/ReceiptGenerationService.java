package com.microsoft.gbb.reddog.receiptgenerationservice.service;

import com.microsoft.gbb.reddog.receiptgenerationservice.dto.OrderSummaryDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.dapr.client.DaprClient;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;

/**
 * Receipt generation service
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReceiptGenerationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptGenerationService.class);
    @Autowired
    private final DaprClient client;    

    public void generateReceipt(OrderSummaryDto orderSummary) {
        LOGGER.info("Generating receipt");
        client.invokeBinding("reddog.binding.receipt", "create", orderSummary).block();
    }
}
