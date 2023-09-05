package com.northwind.product.controller;

import com.northwind.product.request.CategoryRequest;
import com.northwind.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/product-service"})
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(categoryService.getCategories());
    }


    @GetMapping("/category/{id}")
    public ResponseEntity<?> getByCategoryId(@PathVariable int id) {
        return ResponseEntity.ok().body(categoryService.getCategory(id));
    }

    @GetMapping("/category/{id}/product")
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable int id,
                                                     @RequestParam(defaultValue = "0") Integer pageNo,
                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                     @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok().body(categoryService.getProductsByCategory(id, pageNo, pageSize, sortBy));
    }

    @PostMapping("/category")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest body) {
        return ResponseEntity.ok().body(categoryService.saveCategory(body));
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<?> updateByCategoryId(@PathVariable int id, @RequestBody CategoryRequest body) {
        return ResponseEntity.ok().body(categoryService.updateCategory(id, body));
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteByCategoryId(@PathVariable int id) {
        return ResponseEntity.ok().body(categoryService.deleteCategory(id));
    }
}
