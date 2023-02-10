package com.microsoft.gbb.reddog.orderservice.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.microsoft.gbb.reddog.orderservice.dto.ProductDto;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.DaprPreviewClient;
import io.dapr.client.domain.QueryStateRequest;
import io.dapr.client.domain.QueryStateResponse;
import io.dapr.client.domain.State;
import io.dapr.client.domain.query.Query;
import io.dapr.client.domain.query.Sorting;
import io.dapr.client.domain.query.filters.InFilter;
import com.fasterxml.jackson.datatype.jsr310.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Qualifier("productrepository")
public class ProductRepositoryImpl implements ProductRepository {

    
    private final DaprClient client = (new DaprClientBuilder()).build();
    private final String STATE_STORE_NAME = "reddog.statestore.products";

    public ArrayList<ProductDto> findAll() {
        // Query query = new Query()
        // .setFilter(new InFilter<>("categoryId", List.of("coffee", "wine", "other")))
        // .setSort(Arrays.asList(new Sorting("productId", Sorting.Order.DESC)));
        // QueryStateRequest queryStateRequest = new QueryStateRequest(STATE_STORE_NAME)
        //                                      .setQuery(query);
        // QueryStateResponse<ProductDto> result = client.queryState(queryStateRequest, ProductDto.class).block();

        // return new ArrayList<>(
        //         result.getResults()
        //                 .stream()
        //                 .map(r -> r.getValue())
        //                 .toList());
        ProductDto[] products = {
            new ProductDto(1.99, 1, 1.99, "https://daprworkshop.blob.core.windows.net/images/americano.jpg", "Americano", "Americano", "coffee"),
            new ProductDto(2.99, 2, 2.99, "https://daprworkshop.blob.core.windows.net/images/macchiato.jpg", "Caramel Macchiato", "Caramel Macchiato", "coffee"),
            new ProductDto(3.99, 3, 3.99, "https://daprworkshop.blob.core.windows.net/images/latte.jpg", "Latte", "Latte", "coffee"),
            new ProductDto(4.99, 4, 4.99, "https://daprworkshop.blob.core.windows.net/images/cappuccino.jpg", "Cappuccino", "Cappuccino", "coffee"),
            new ProductDto(5.99, 5, 5.99, "https://daprworkshop.blob.core.windows.net/images/espresso.jpg", "Espresso", "Espresso", "coffee"),
            new ProductDto(6.99, 6, 6.99, "https://daprworkshop.blob.core.windows.net/images/mocha.jpg", "Mocha", "Mocha", "coffee"),
        };
        client.saveState(STATE_STORE_NAME, "products", products).block();
        State<ProductDto[]> retrievedMessage = client.getState(STATE_STORE_NAME, "products", ProductDto[].class).block();
        return new ArrayList<>(Arrays.asList(retrievedMessage.getValue()));
    }
}