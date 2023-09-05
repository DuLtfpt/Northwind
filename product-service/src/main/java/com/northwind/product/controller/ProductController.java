package com.northwind.product.controller;

import com.northwind.product.request.ProductRequest;
import com.northwind.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/product-service"})
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "id") String sortBy){
        return ResponseEntity.ok().body(productService.orderProducts(pageNo,pageSize,sortBy));
    }

    @PostMapping("/product-order")
    public ResponseEntity<?> getByIterable(@RequestBody Map<Integer, Integer> body){
        return ResponseEntity.ok().body(productService.orderProducts(body));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getByProductId(@PathVariable int id){
        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    @PostMapping("/product")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest body){
        return ResponseEntity.ok().body(productService.saveProduct(body));
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateByProductId(@PathVariable int id, @RequestBody ProductRequest body){
        return ResponseEntity.ok().body(productService.updateProduct(id, body));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteByProductId(@PathVariable int id){
        return ResponseEntity.ok().body(productService.setActiveProduct(id));
    }

}
