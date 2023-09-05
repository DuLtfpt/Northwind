package com.northwind.product.model.dto;

public interface ProductDTO {
    Integer getId();

    String getProductName();

    String getProductDetails();

    double getProductPrice();

    int getStock();

    boolean isActive();
}
