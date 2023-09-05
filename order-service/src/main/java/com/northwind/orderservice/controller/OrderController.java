package com.northwind.orderservice.controller;


import com.northwind.orderservice.request.OrderRequest;
import com.northwind.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping({"/order-service"})
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping("/user")
    public ResponseEntity<?> getOrdersByUser(@RequestHeader("user-uid") String id,
                                             @RequestParam(defaultValue = "0") Integer pageNo,
                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                             @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok().body(service.getOrdersByUser(id,pageNo, pageSize, sortBy));
    }

    @GetMapping("/order")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok().body(service.getOrders(pageNo, pageSize, sortBy));
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrder(@PathVariable String id) {
        return ResponseEntity.ok().body(service.getOrderById(id));
    }

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestHeader("user-uid") String uid, @RequestBody List<OrderRequest> body) {
        return ResponseEntity.ok().body(service.createOrder(uid,body));
    }
}
