package com.microsoft.gbb.reddog.makelineservice.repository;
import com.microsoft.gbb.reddog.makelineservice.dto.OrderSummaryDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface OrderSummaryRepository {
    ArrayList<OrderSummaryDto> getOrdersForStore(String storeId);
    List<OrderSummaryDto> findAllByStoreId(String storeId);
    OrderSummaryDto findByOrderIdAndStoreId(String orderId, String storeId);
    List<OrderSummaryDto> findByOrderId(String orderId);
    OrderSummaryDto saveOrder(OrderSummaryDto order);
}