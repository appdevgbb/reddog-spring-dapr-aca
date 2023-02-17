package com.microsoft.gbb.reddog.accountingservice.repository;

import com.microsoft.gbb.reddog.accountingservice.dto.ChartKeyValue;
import com.microsoft.gbb.reddog.accountingservice.dto.OrderSummaryDto;
import com.microsoft.gbb.reddog.accountingservice.dto.OrdersTimeSeries;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSummaryRepository
{
    List<OrderSummaryDto> findAllOrders();
    List<OrderSummaryDto> findAllOrdersForStore(String storeId);
    List<OrderSummaryDto> findAllInflightOrders();
    List<OrderSummaryDto> findAllCompletedOrders();
    List<OrderSummaryDto> findAllInflightOrdersForStore(String storeId);
    OrdersTimeSeries getOrderCountForThePastTimeSpan( String period,
                                                      String timeSpan,
                                                     String storeId);
    OrdersTimeSeries getOrderCountWithinTimeInterval(String period,
                                           String timeStart,
                                           String timeEnd,
                                           String storeId);
    List<ChartKeyValue<Long>> getOrderCountByDay(String storeId);
}