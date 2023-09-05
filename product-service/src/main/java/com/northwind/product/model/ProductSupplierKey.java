package com.northwind.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ProductSupplierKey implements Serializable {
    @Column(name = "supplier_id")
    private Integer supplierId;
    @Column(name = "product_id")
    private Integer productId;
}
