package com.microsoft.gbb.reddog.makelineservice.service;

import com.microsoft.gbb.reddog.makelineservice.dto.OrderSummaryDto;
import com.microsoft.gbb.reddog.makelineservice.messaging.TopicProducer;
import com.microsoft.gbb.reddog.makelineservice.repository.OrderSummaryRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Order service.
 */
@Slf4j
@Component
public class MakelineService {
    private final OrderSummaryRepository orderSummaryRepository;
    private final TopicProducer topicProducer;

    public MakelineService(OrderSummaryRepository orderSummaryRepository, TopicProducer topicProducer) {
        this.orderSummaryRepository = orderSummaryRepository;
        this.topicProducer = topicProducer;
    }
    
    public OrderSummaryDto addOrderToMakeLine(OrderSummaryDto orderSummary) {
        log.info("Adding order to make line {}", orderSummary.toString());
        orderSummary.setIsCompleted("false");
        return orderSummaryRepository.saveOrder(orderSummary);
    }

    public List<OrderSummaryDto> getOrders(String storeId) {
        log.info("Getting all orders for storeId: " + storeId);
       return orderSummaryRepository.findAllByStoreId(storeId);
    }

    public OrderSummaryDto completeOrderForStore(String storeId, String orderId) {
        log.info("Completing order for storeId: " + storeId + " orderId: " + orderId);
        OrderSummaryDto orderSummary = orderSummaryRepository.findByOrderIdAndStoreId(orderId, storeId);
        orderSummary.setOrderCompletedInstant(LocalDate.now().toEpochDay());
        orderSummary.setIsCompleted("true");
        topicProducer.send(orderSummary);
        log.info("Order completed: " + orderSummary);
        return orderSummaryRepository.saveOrder(orderSummary);
    }

    // complete order for order id
    public OrderSummaryDto completeOrder(String orderId) {
        log.info("Completing order for orderId: " + orderId);
        OrderSummaryDto orderSummary = orderSummaryRepository.findByOrderId(orderId).get(0);
        orderSummary.setOrderCompletedInstant(LocalDate.now().toEpochDay());
        topicProducer.send(orderSummary);
        log.info("Order completed: " + orderSummary);
        return orderSummaryRepository.saveOrder(orderSummary);
    }
}
