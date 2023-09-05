package com.northwind.product.repository;

import com.northwind.product.model.Category;
import com.northwind.product.model.Product;
import com.northwind.product.model.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Page<ProductDTO> findAllByCategory(Category category, Pageable pageable);
}
