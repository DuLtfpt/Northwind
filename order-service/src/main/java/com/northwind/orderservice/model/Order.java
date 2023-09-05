package com.northwind.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    private UUID id;
    @Column(name = "uid")
    private String uid;
    @Column(name = "order_date")
    private Date orderDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id.orderId")
    private Collection<OrderDetail> orderDetails;
}
