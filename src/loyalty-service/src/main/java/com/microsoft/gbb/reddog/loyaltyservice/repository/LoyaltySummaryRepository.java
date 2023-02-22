package com.microsoft.gbb.reddog.loyaltyservice.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;

import com.microsoft.gbb.reddog.loyaltyservice.model.LoyaltySummary;

import io.dapr.client.DaprClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Qualifier("loyaltySummaryRepository")
@RequiredArgsConstructor
public class LoyaltySummaryRepository {

    @Autowired
    private final DaprClient client;
    private final String stateStoreName = "reddog.statestore.loyalty";

    public LoyaltySummary save(LoyaltySummary loyaltySummary){
        client.saveState(stateStoreName, loyaltySummary.getLoyaltyId(), loyaltySummary).block();
        return loyaltySummary;
    }

}
