package com.microsoft.gbb.reddog.makelineservice.controller;

import com.microsoft.gbb.reddog.makelineservice.dto.OrderSummaryDto;
import com.microsoft.gbb.reddog.makelineservice.exception.SaveOrderException;
import com.microsoft.gbb.reddog.makelineservice.service.MakelineService;

import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class MakelineController {

    private final MakelineService makelineService;

    public MakelineController(MakelineService makelineService) {
        this.makelineService = makelineService;
    }

    @Topic(name = "orders", pubsubName = "reddog.pubsub")
    @PostMapping(value = "/orders")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "*")
    public ResponseEntity<OrderSummaryDto> addOrderToMakeLine(@RequestBody CloudEvent<OrderSummaryDto> cloudEvent) {
        var orderSummary = cloudEvent.getData();
        if (null == orderSummary) {
            throw new SaveOrderException("OrderSummary is empty");
        }
        return ResponseEntity.ok(makelineService.addOrderToMakeLine(orderSummary));
    }

    @GetMapping(value = "/orders/{storeId}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<OrderSummaryDto>> getOrders(@PathVariable String storeId) {
        if (null == storeId) {
            throw new SaveOrderException("Store ID is empty");
        }
        return ResponseEntity.ok(makelineService.getOrders(storeId));
    }

    @DeleteMapping(value = "/orders/{storeId}/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CrossOrigin(origins = "*")
    public ResponseEntity<OrderSummaryDto> completeOrder(@PathVariable String storeId, @PathVariable String orderId) {
        if (null == storeId || null == orderId) {
            throw new SaveOrderException("Store ID or Order ID is empty");
        }
        return ResponseEntity.ok(makelineService.completeOrderForStore(storeId, orderId));
    }

    // complete order for a given order id
    @DeleteMapping(value = "/orders/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CrossOrigin(origins = "*")
    public ResponseEntity<OrderSummaryDto> completeOrder(@PathVariable String orderId) {
        if (null == orderId) {
            throw new SaveOrderException("Order ID is empty");
        }
        return ResponseEntity.ok(makelineService.completeOrder(orderId));
    }
}
