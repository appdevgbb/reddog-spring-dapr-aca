package com.microsoft.gbb.reddog.orderservice.messaging;

import com.microsoft.gbb.reddog.orderservice.dto.OrderSummaryDto;

import io.dapr.client.DaprClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicProducer {
    @Autowired
    private final DaprClient client;
    private final String pubsubName = "reddog.pubsub";
    private final String topicName = "orders";

    public void send(OrderSummaryDto message){
        log.info("Payload: {}", message);
        client.publishEvent(pubsubName, topicName, message).block();
    }
}
