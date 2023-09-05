package com.northwind.product.service;

import com.northwind.product.model.ProductSupplier;
import com.northwind.product.model.Supplier;
import com.northwind.product.repository.SupplierRepository;
import com.northwind.product.request.SupplierRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupplierService {

    @Value("${message.supplier-create-successfully}")
    private String createSupplierSuccessfully;
    @Value("${message.supplier-update-successfully}")
    private String updateSupplierSuccessfully;
    @Value("${message.supplier-delete-successfully}")
    private String deleteSupplierSuccessfully;


    @Autowired
    private SupplierRepository repository;

    @Autowired
    private ProductSupplierService productSupplierService;

    public List<Supplier> getSuppliers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Supplier> pagedResult = repository.findAll(pageable);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return null;
    }

    public Supplier getSupplierById(int id) {
        return repository.findById(id).orElse(null);
    }


    public String addSupplier(SupplierRequest body) {
        repository.save(
                new Supplier(
                        null,
                        body.getName(),
                        body.getAddress(),
                        body.getPhone(),
                        body.isActive()
                )
        );
        return createSupplierSuccessfully;
    }


    public String updateSupplier(int id, SupplierRequest body) {
        repository.save(
                new Supplier(
                        id,
                        body.getName(),
                        body.getAddress(),
                        body.getPhone(),
                        body.isActive()
                )
        );
        return updateSupplierSuccessfully;
    }

    public String deleteSupplier(int id) {
        Supplier supplier = repository.findById(id).orElse(null);
        if (null == supplier) {
            return deleteSupplierSuccessfully;
        }
        List<ProductSupplier> productSuppliers = productSupplierService
                .getBySupplierId(id, 0, 1, "supplierId");
        if (null == productSuppliers) {
            repository.delete(supplier);
            return deleteSupplierSuccessfully;
        }
        supplier.setActive(false);
        repository.save(supplier);
        return updateSupplierSuccessfully;
    }

    public Map<?, ?> getProductsBySupplierId(int id, int pageNo, int pageSize, String sortBy) {
        Supplier supplier = getSupplierById(id);
        if (null == supplier) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("supplier", supplier);
        List<ProductSupplier> productSuppliers = productSupplierService
                .getBySupplierId(id, pageNo, pageSize, sortBy);
        result.put("products",productSuppliers);
        return result;
    }
}
