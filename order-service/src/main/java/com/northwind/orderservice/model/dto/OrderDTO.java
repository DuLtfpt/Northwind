package com.northwind.orderservice.model.dto;

import java.util.Date;
import java.util.UUID;

public interface OrderDTO {
    UUID getId();
    String getUid();
    Date getOrderDate();
}
