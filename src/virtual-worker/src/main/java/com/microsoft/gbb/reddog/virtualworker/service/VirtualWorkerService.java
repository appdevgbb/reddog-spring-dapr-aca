package com.microsoft.gbb.reddog.virtualworker.service;

import com.microsoft.gbb.reddog.virtualworker.dto.OrderSummaryDto;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.HttpExtension;
import io.leego.banana.Ansi;
import io.leego.banana.BananaUtils;
import io.leego.banana.Font;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Order service.
 */
@Slf4j
@Component
public class VirtualWorkerService {
    private final DaprClient client = (new DaprClientBuilder()).build();
    private final String makelineService = "makeline-service";

    public void checkOrders(String storeId) {
        log.info("Checking orders for store: {}", storeId);
        List<OrderSummaryDto> orders = getOrders(storeId);
        orders.forEach(this::completeOrder);
        log.info("Check Orders");

    }

    public List<OrderSummaryDto> getOrders(String storeId) {
        
        var orders = client.invokeMethod(makelineService, "orders/" + storeId, null, HttpExtension.GET, OrderSummaryDto[].class).block();
        return List.of(orders);
    }

    public void completeOrder(OrderSummaryDto orderSummary) {
        String logMessage = MessageFormat.format("\nCompleting order for {0} @ store {1}\n",
                orderSummary.getFirstName(),
                orderSummary.getStoreId());
        log.info(BananaUtils.bananansi(logMessage, Font.THREE_POINT, Ansi.PURPLE));
        client.invokeMethod(makelineService, "orders/" + orderSummary.getStoreId()
                             + "/" + orderSummary.getOrderId(), null, 
                             HttpExtension.DELETE, OrderSummaryDto.class).block();
        
    }

    public List<OrderSummaryDto> completeOrders(String[] orderIds) {
        List<OrderSummaryDto> orders = new ArrayList<>();
        for (String orderId : orderIds) {
            var order = client.invokeMethod(makelineService, "orders/" + orderId, null, 
                             HttpExtension.DELETE, OrderSummaryDto.class).block();
            orders.add(order);
        }
        return orders;
    }
}
