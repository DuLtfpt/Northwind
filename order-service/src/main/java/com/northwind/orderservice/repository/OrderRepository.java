package com.northwind.orderservice.repository;

import com.northwind.orderservice.model.Order;
import com.northwind.orderservice.model.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<OrderDTO> findAllProjectedBy(Pageable pageable);
    Page<OrderDTO> findProjectedByUid(String id, Pageable pageable);
}
