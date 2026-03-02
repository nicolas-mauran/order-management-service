package com.example.ordermanagement.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Order {

  private final UUID id;
  private final String symbol;
  private final OrderSide side;
  private final OrderType type;
  private final long quantity;
  private final BigDecimal limitPrice;
  private final Instant createdAt;
  private OrderStatus status;

  private Order(
      UUID id,
      String symbol,
      OrderSide side,
      OrderType type,
      long quantity,
      BigDecimal limitPrice,
      Instant createdAt,
      OrderStatus status) {
    this.id = Objects.requireNonNull(id, "id is required");
    this.symbol = requireSymbol(symbol);
    this.side = Objects.requireNonNull(side, "side is required");
    this.type = Objects.requireNonNull(type, "type is required");
    this.quantity = requirePositiveQuantity(quantity);
    this.limitPrice = validatePrice(type, limitPrice);
    this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
    this.status = Objects.requireNonNull(status, "status is required");
  }

  public static Order createNew(
      String symbol, OrderSide side, OrderType type, long quantity, BigDecimal limitPrice) {
    return new Order(
        UUID.randomUUID(), symbol, side, type, quantity, limitPrice, Instant.now(), OrderStatus.NEW);
  }

  public static Order restore(
      UUID id,
      String symbol,
      OrderSide side,
      OrderType type,
      long quantity,
      BigDecimal limitPrice,
      Instant createdAt,
      OrderStatus status) {
    return new Order(id, symbol, side, type, quantity, limitPrice, createdAt, status);
  }

  public void cancel() {
    if (status != OrderStatus.NEW) {
      throw new IllegalStateException("Only NEW orders can be cancelled");
    }
    status = OrderStatus.CANCELLED;
  }

  public void fill() {
    if (status != OrderStatus.NEW) {
      throw new IllegalStateException("Only NEW orders can be filled");
    }
    status = OrderStatus.FILLED;
  }

  public UUID id() {
    return id;
  }

  public String symbol() {
    return symbol;
  }

  public OrderSide side() {
    return side;
  }

  public OrderType type() {
    return type;
  }

  public long quantity() {
    return quantity;
  }

  public BigDecimal limitPrice() {
    return limitPrice;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public OrderStatus status() {
    return status;
  }

  private static String requireSymbol(String rawSymbol) {
    if (rawSymbol == null || rawSymbol.isBlank()) {
      throw new IllegalArgumentException("symbol is required");
    }
    return rawSymbol.trim().toUpperCase();
  }

  private static long requirePositiveQuantity(long quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("quantity must be > 0");
    }
    return quantity;
  }

  private static BigDecimal validatePrice(OrderType type, BigDecimal limitPrice) {
    if (type == OrderType.MARKET) {
      if (limitPrice != null) {
        throw new IllegalArgumentException("limitPrice must be null for MARKET orders");
      }
      return null;
    }

    if (limitPrice == null) {
      throw new IllegalArgumentException("limitPrice is required for LIMIT orders");
    }
    if (limitPrice.signum() <= 0) {
      throw new IllegalArgumentException("limitPrice must be > 0");
    }
    return limitPrice;
  }
}

