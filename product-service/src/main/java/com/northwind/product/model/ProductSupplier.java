package com.northwind.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_supplier")
public class ProductSupplier {
    @EmbeddedId
    ProductSupplierKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @MapsId("supplierId")
    @JoinColumn(name = "supplier_id")
    Supplier supplier;

    @Column(name = "import_date")
    private Date import_date;
    @Column(name = "quantity")
    private int quantity;
}
