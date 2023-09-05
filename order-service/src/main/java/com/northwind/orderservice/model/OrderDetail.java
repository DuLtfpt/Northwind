package com.northwind.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @EmbeddedId
    private OrderDetailKey id;
    @Column(name = "unit_pirce")
    private double unit_pirce;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "discount")
    private float discount;
}
