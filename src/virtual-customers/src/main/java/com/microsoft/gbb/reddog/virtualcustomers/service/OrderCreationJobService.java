package com.microsoft.gbb.reddog.virtualcustomers.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.microsoft.gbb.reddog.virtualcustomers.model.CustomerOrder;
import com.microsoft.gbb.reddog.virtualcustomers.model.Product;
import com.microsoft.gbb.reddog.virtualcustomers.util.CustomerGenerator;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.HttpExtension;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.spring.annotations.Recurring;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Order service.
 */
@Slf4j
@Component
public class OrderCreationJobService {

    public static final String ORIGIN = "jobrunr";

    @Value("${data.STORE_ID}")
    private String STORE_ID;

    private final DaprClient client = (new DaprClientBuilder()).build();
    private final String orderService = "order-service";

    private final CustomerGenerator customerGenerator;


    public OrderCreationJobService(CustomerGenerator customerGenerator) {
        this.customerGenerator = customerGenerator;
    }
    
    @Recurring(id = "create-orders", cron = "#{'${data.CREATE_ORDER_CRON_SCHEDULE}'}")
    @Job(name = "Virtual Customers")
    public void execute() {
        log.info("Creating orders");
        // TODO: add additional configs similar to .NET
        List<Product> products = getProducts();
        if (products.isEmpty()) {
            log.info("No products to generate orders for. Exiting.");
            return;
        }
        CustomerOrder customerOrder = createCustomerOrder(products, ORIGIN);
        log.info("Created order: {}", customerOrder);
    }

    public List<CustomerOrder> createOrders(int numOrders, String origin) {
        List<CustomerOrder> orders = new ArrayList<>();
        List<Product> products = getProducts();
        if (products.isEmpty()) {
            log.info("No products to generate orders for. Exiting.");
            return orders;
        }
        for (int i = 0; i < numOrders; i++) {
            orders.add(createCustomerOrder(products, origin));
        }
        return orders;
    }

    private CustomerOrder createCustomerOrder(List<Product> products, String origin) {
        CustomerOrder order = CustomerOrder.builder()
                .storeId(customerGenerator.generateStoreId())
                .firstName(customerGenerator.generateFirstName())
                .lastName(customerGenerator.generateLastName())
                .loyaltyId(String.valueOf(customerGenerator.generateLoyaltyId()))
                .orderItems(customerGenerator.generateOrderItems(products))
                .storeLatitude(customerGenerator.generateLatitude())
                .storeLongitude(customerGenerator.generateLongitude())
                .origin(origin)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String orderJson = null;
        try {
            orderJson = mapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing order to JSON {}", e);
        }
        log.info("Creating order: {}", orderJson);
        
        return client.invokeMethod(orderService, "order", order, HttpExtension.POST, CustomerOrder.class).block();
    }

    private List<Product> getProducts() {
        var products = client.invokeMethod(orderService, "products" , null, HttpExtension.GET, Product[].class).block();
        return List.of(products);
    }
}
