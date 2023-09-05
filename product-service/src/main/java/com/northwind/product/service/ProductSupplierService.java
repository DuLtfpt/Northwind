package com.northwind.product.service;

import com.northwind.product.model.ProductSupplier;
import com.northwind.product.repository.ProductSupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSupplierService {

    @Autowired
    private ProductSupplierRepository repository;

    public List<ProductSupplier> getBySupplierId(int supplierId,Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<ProductSupplier> pagedResult = repository.findAllBySupplierId(supplierId, pageable);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return null;
    }
}
