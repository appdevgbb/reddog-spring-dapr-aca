package com.microsoft.gbb.reddog.makelineservice.messaging;

import com.microsoft.gbb.reddog.makelineservice.dto.OrderSummaryDto;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TopicProducer {

    private final DaprClient client = (new DaprClientBuilder()).build();
    private final String pubsubName = "reddog.pubsub";
    private final String topicName = "ordercompleted";

    public void send(OrderSummaryDto message){
        log.info("Publishing:  {}", message);
        client.publishEvent(pubsubName,topicName, message);
    }

}
