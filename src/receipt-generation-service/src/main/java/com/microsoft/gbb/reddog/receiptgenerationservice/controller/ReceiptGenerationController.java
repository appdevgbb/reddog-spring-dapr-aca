package com.microsoft.gbb.reddog.receiptgenerationservice.controller;

import com.microsoft.gbb.reddog.receiptgenerationservice.dto.OrderSummaryDto;
import com.microsoft.gbb.reddog.receiptgenerationservice.exception.ReceiptSaveException;
import com.microsoft.gbb.reddog.receiptgenerationservice.service.ReceiptGenerationService;

import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ReceiptGenerationController {

    private final ReceiptGenerationService receiptGenerationService;

    public ReceiptGenerationController(ReceiptGenerationService receiptGenerationService) {
        this.receiptGenerationService = receiptGenerationService;
    }

    @Topic(name = "orders", pubsubName = "reddog.pubsub")
    @PostMapping(value = "/orders")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> generateReceipt(@RequestBody CloudEvent<OrderSummaryDto> cloudEvent) {
        var orderSummary = cloudEvent.getData();
        if (null == orderSummary) {
            throw new ReceiptSaveException("Order is null");
        }
        receiptGenerationService.generateReceipt(orderSummary);
        log.info("Successfully generated receipt: ");
        return ResponseEntity.ok(orderSummary.toString());
    }
}