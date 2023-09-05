package com.northwind.product.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {
    private String name;
    private String address;
    private String phone;
    private boolean active;
}
