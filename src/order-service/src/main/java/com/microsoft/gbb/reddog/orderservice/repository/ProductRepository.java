package com.microsoft.gbb.reddog.orderservice.repository;

import com.microsoft.gbb.reddog.orderservice.dto.ProductDto;


import java.util.ArrayList;

public interface ProductRepository {
    ArrayList<ProductDto> findAll();
}


