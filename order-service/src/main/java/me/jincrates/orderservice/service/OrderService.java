package me.jincrates.orderservice.service;

import me.jincrates.orderservice.dto.OrderDto;
import me.jincrates.orderservice.jpa.OrderEntity;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);

    OrderDto getOrderByOrderId(String orderId);

    Iterable<OrderEntity> getOrdersByUserId(String userId);
}
