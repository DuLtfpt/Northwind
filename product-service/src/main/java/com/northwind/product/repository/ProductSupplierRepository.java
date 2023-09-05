package com.northwind.product.repository;

import com.northwind.product.model.ProductSupplier;
import com.northwind.product.model.ProductSupplierKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, ProductSupplierKey>{
    public Page<ProductSupplier> findAllBySupplierId(int supplierId, Pageable pageable);
    public Page<ProductSupplier> findAllByProductId(int productId, Pageable pageable);
}
