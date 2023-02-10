package com.microsoft.gbb.reddog.orderservice.messaging;

import com.microsoft.gbb.reddog.orderservice.dto.OrderSummaryDto;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TopicProducer {
    private final DaprClient client = (new DaprClientBuilder()).build();
    private final String pubsubName = "reddog.pubsub";
    private final String topicName = "orders";

    public void send(OrderSummaryDto message){
        log.info("Payload: {}", message);
        client.publishEvent(pubsubName, topicName, message).block();
    }

}
