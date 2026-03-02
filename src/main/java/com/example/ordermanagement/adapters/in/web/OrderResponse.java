package com.example.ordermanagement.adapters.in.web;

import com.example.ordermanagement.domain.Order;
import com.example.ordermanagement.domain.OrderSide;
import com.example.ordermanagement.domain.OrderStatus;
import com.example.ordermanagement.domain.OrderType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    String symbol,
    OrderSide side,
    OrderType type,
    long quantity,
    BigDecimal limitPrice,
    OrderStatus status,
    Instant createdAt) {

  static OrderResponse from(Order order) {
    return new OrderResponse(
        order.id(),
        order.symbol(),
        order.side(),
        order.type(),
        order.quantity(),
        order.limitPrice(),
        order.status(),
        order.createdAt());
  }
}

