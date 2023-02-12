package com.microsoft.gbb.reddog.orderservice.service;

import com.microsoft.gbb.reddog.orderservice.dto.ProductDto;
import com.microsoft.gbb.reddog.orderservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
         this.productRepository = productRepository;
    }

    public List<ProductDto> getAllProducts() {
         return productRepository.findAll();
    }
}
