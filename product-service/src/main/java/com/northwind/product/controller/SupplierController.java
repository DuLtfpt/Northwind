package com.northwind.product.controller;

import com.northwind.product.request.SupplierRequest;
import com.northwind.product.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/product-service"})
public class SupplierController {


    @Autowired
    private SupplierService service;

    @GetMapping("/supplier")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok().body(service.getSuppliers(pageNo, pageSize, sortBy));
    }

    @GetMapping("/supplier/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return ResponseEntity.ok().body(service.getSupplierById(id));
    }

    @GetMapping("/supplier/{id}/product")
    public ResponseEntity<?> getById(@PathVariable int id,
                                     @RequestParam(defaultValue = "0") Integer pageNo,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok().body(service.getProductsBySupplierId(id, pageNo, pageSize, sortBy));
    }

    @PostMapping("/supplier")
    public ResponseEntity<?> addSupplier(@RequestBody SupplierRequest body) {
        return ResponseEntity.ok().body(service.addSupplier(body));
    }

    @PutMapping("/supplier/{id}")
    public ResponseEntity<?> updateBySupplierId(@PathVariable int id, @RequestBody SupplierRequest body) {
        return ResponseEntity.ok().body(service.updateSupplier(id, body));
    }

    @DeleteMapping("/supplier/{id}")
    public ResponseEntity<?> deleteBySupplierId(@PathVariable int id) {
        return ResponseEntity.ok().body(service.deleteSupplier(id));
    }
}
