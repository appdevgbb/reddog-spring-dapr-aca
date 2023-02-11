package com.microsoft.gbb.reddog.loyaltyservice.controller;

import com.microsoft.gbb.reddog.loyaltyservice.dto.OrderSummaryDto;
import com.microsoft.gbb.reddog.loyaltyservice.exception.LoyaltySaveException;
import com.microsoft.gbb.reddog.loyaltyservice.model.LoyaltySummary;
import com.microsoft.gbb.reddog.loyaltyservice.service.LoyaltyService;

import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    public LoyaltyController(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    @Topic(name = "orders", pubsubName = "reddog.pubsub")
    @PostMapping(value = "/orders")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "*")
    public ResponseEntity<LoyaltySummary> updateLoyalty(@RequestBody CloudEvent<OrderSummaryDto> cloudEvent) {
        var orderSummary = cloudEvent.getData();
        if (null == orderSummary) {
            throw new LoyaltySaveException("OrderSummary is empty");
        }
        return ResponseEntity.ok(loyaltyService.updateLoyalty(orderSummary));
    }

}
