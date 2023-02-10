package com.microsoft.gbb.reddog.orderservice.repository;

import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.microsoft.gbb.reddog.orderservice.dto.ProductDto;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.State;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Qualifier("productrepository")
public class ProductRepositoryImpl implements ProductRepository {

    private final DaprClient client = (new DaprClientBuilder()).build();
    private final String stateStoreName = "reddog.statestore.products";

    public ArrayList<ProductDto> findAll() {
        var products = getProducts();
        if (products == null)
            products = populateProducts();
        return new ArrayList<>(Arrays.asList(products));
    }

    private ProductDto[] populateProducts() {
        ProductDto[] products = {
                new ProductDto(1.99, 1, 1.99, "https://daprworkshop.blob.core.windows.net/images/americano.jpg",
                        "Americano", "Americano", "coffee"),
                new ProductDto(2.99, 2, 2.99, "https://daprworkshop.blob.core.windows.net/images/macchiato.jpg",
                        "Caramel Macchiato", "Caramel Macchiato", "coffee"),
                new ProductDto(3.99, 3, 3.99, "https://daprworkshop.blob.core.windows.net/images/latte.jpg", "Latte",
                        "Latte", "coffee"),
                new ProductDto(4.99, 4, 4.99, "https://daprworkshop.blob.core.windows.net/images/cappuccino.jpg",
                        "Cappuccino", "Cappuccino", "coffee"),
                new ProductDto(5.99, 5, 5.99, "https://daprworkshop.blob.core.windows.net/images/espresso.jpg",
                        "Espresso", "Espresso", "coffee"),
                new ProductDto(6.99, 6, 6.99, "https://daprworkshop.blob.core.windows.net/images/mocha.jpg", "Mocha",
                        "Mocha", "coffee"),
        };
        client.saveState(stateStoreName, "products", products).block();
        return products;
    }

    private ProductDto[] getProducts() {
        State<ProductDto[]> retrievedMessage = client.getState(stateStoreName, "products", ProductDto[].class)
                .block();
        return retrievedMessage.getValue();
    }
}