package com.northwind.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
    private Integer id;
    private String productName;
    private String productDetails;
    private double productPrice;
    private int stock;
    private boolean active;
}
