package com.microsoft.gbb.reddog.accountingservice.repository;

import com.microsoft.gbb.reddog.accountingservice.dto.ChartKeyValue;
import com.microsoft.gbb.reddog.accountingservice.dto.OrderSummaryDto;
import com.microsoft.gbb.reddog.accountingservice.dto.OrdersTimeSeries;

import io.dapr.client.DaprPreviewClient;
import io.dapr.client.domain.QueryStateRequest;
import io.dapr.client.domain.QueryStateResponse;
import io.dapr.client.domain.query.Pagination;
import io.dapr.client.domain.query.Query;
import io.dapr.client.domain.query.filters.AndFilter;
import io.dapr.client.domain.query.filters.EqFilter;
import io.dapr.client.domain.query.filters.Filter;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("orderSummaryRepository")
@RequiredArgsConstructor
public class OrderSummaryRepositoryImpl implements OrderSummaryRepository {
    
    @Autowired
    private final DaprPreviewClient previewClient;

    private final String stateStoreName = "reddog.statestore.orders";

    private List<OrderSummaryDto> findBy(Filter filter) {
        var results = new ArrayList<OrderSummaryDto>();
        String token = null;
        do {
            Query query = new Query()
                    .setFilter(filter)
                    .setPagination(new Pagination(300, token));
            QueryStateRequest request = new QueryStateRequest(stateStoreName)
                    .setQuery(query);

            QueryStateResponse<OrderSummaryDto> result = previewClient.queryState(request, OrderSummaryDto.class).block();
            results.addAll(result.getResults().stream().map(r -> r.getValue()).toList());
            token = result.getToken();
        } while (StringUtils.isNotEmpty(token));

        return results;
    }

    @Override
    public List<OrderSummaryDto> findAllOrders() {
        return findBy(new EqFilter<>("allHack", "0"));
    }

    @Override
    public List<OrderSummaryDto> findAllOrdersForStore(String storeId) {
        return findBy(new EqFilter<>("storeId", storeId));
    }

    @Override
    public List<OrderSummaryDto> findAllInflightOrders() {
        return findBy(new EqFilter<>("isCompleted", "false"));
    }
    
    @Override
    public List<OrderSummaryDto> findAllCompletedOrders() {
        return findBy(new EqFilter<>("isCompleted", "true"));
    }

    @Override
    public List<OrderSummaryDto> findAllInflightOrdersForStore(String storeId) {
        return findBy(new AndFilter()
        .addClause(new EqFilter<>("storeId", storeId))
        .addClause(new EqFilter<>("isCompleted", "false")));
    }

    @Override
    public OrdersTimeSeries getOrderCountForThePastTimeSpan(String period, String timeSpan, String storeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OrdersTimeSeries getOrderCountWithinTimeInterval(String period, String timeStart, String timeEnd,
            String storeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ChartKeyValue<Long>> getOrderCountByDay(String storeId) {
        // TODO Auto-generated method stub
        return null;
    }
}