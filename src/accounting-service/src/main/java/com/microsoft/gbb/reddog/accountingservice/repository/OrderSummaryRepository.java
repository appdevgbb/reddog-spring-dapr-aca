package com.microsoft.gbb.reddog.accountingservice.repository;

import com.microsoft.gbb.reddog.accountingservice.dto.ChartKeyValue;
import com.microsoft.gbb.reddog.accountingservice.dto.OrderSummaryDto;
import com.microsoft.gbb.reddog.accountingservice.dto.OrdersTimeSeries;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSummaryRepository
{
    List<OrderSummaryDto> findAllOrders();
    List<OrderSummaryDto> findAllOrdersForStore(@Param("storeId") String storeId);
    List<OrderSummaryDto> findAllInflightOrders();
    List<OrderSummaryDto> findAllCompletedOrders();
    List<OrderSummaryDto> findAllInflightOrdersForStore(@Param("storeId") String storeId);
    OrdersTimeSeries getOrderCountForThePastTimeSpan(@Param("period") String period,
                                                     @Param("timeSpan") String timeSpan,
                                                     @Param("storeId") String storeId);
    OrdersTimeSeries getOrderCountWithinTimeInterval(@Param("period") String period,
                                           @Param("timeStart") String timeStart,
                                           @Param("timeEnd") String timeEnd,
                                           @Param("storeId") String storeId);
    List<ChartKeyValue<Long>> getOrderCountByDay(String storeId);
}