package com.northwind.product.repository;

import com.northwind.product.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier,Integer> {
}
