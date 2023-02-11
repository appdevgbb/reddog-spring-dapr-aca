package com.microsoft.gbb.reddog.loyaltyservice.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.microsoft.gbb.reddog.loyaltyservice.model.LoyaltySummary;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Qualifier("loyaltySummaryRepository")
public class LoyaltySummaryRepository {
    private final DaprClient client = (new DaprClientBuilder()).build();
    private final String stateStoreName = "reddog.statestore.loyalty";

    public LoyaltySummary save(LoyaltySummary loyaltySummary){
        client.saveState(stateStoreName, loyaltySummary.getLoyaltyId(), loyaltySummary).block();
        return loyaltySummary;
    }

}
