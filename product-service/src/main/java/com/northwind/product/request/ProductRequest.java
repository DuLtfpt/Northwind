package com.northwind.product.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String productName;
    private String productDetails;
    private double productPrice;
    private int stock;
    private int categoryId;
    private boolean active;
}
