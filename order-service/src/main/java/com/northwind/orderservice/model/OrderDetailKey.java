package com.northwind.orderservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderDetailKey implements Serializable {
    @Column(name = "product_id")
    private Integer productId;
    @Column(name = "order_id")
    @JoinColumn
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    private UUID orderId;
}
