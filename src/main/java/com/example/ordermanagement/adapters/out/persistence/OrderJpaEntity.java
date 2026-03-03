package com.example.ordermanagement.adapters.out.persistence;

import com.example.ordermanagement.domain.OrderSide;
import com.example.ordermanagement.domain.OrderStatus;
import com.example.ordermanagement.domain.OrderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderJpaEntity {

  @Id private UUID id;

  @Column(nullable = false, length = 32)
  private String symbol;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 8)
  private OrderSide side;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 8)
  private OrderType type;

  @Column(nullable = false)
  private long quantity;

  @Column(precision = 19, scale = 4)
  private BigDecimal limitPrice;

  @Column(nullable = false)
  private Instant createdAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 16)
  private OrderStatus status;

  protected OrderJpaEntity() {}

  public OrderJpaEntity(
      UUID id,
      String symbol,
      OrderSide side,
      OrderType type,
      long quantity,
      BigDecimal limitPrice,
      Instant createdAt,
      OrderStatus status) {
    this.id = id;
    this.symbol = symbol;
    this.side = side;
    this.type = type;
    this.quantity = quantity;
    this.limitPrice = limitPrice;
    this.createdAt = createdAt;
    this.status = status;
  }

  public UUID getId() {
    return id;
  }

  public String getSymbol() {
    return symbol;
  }

  public OrderSide getSide() {
    return side;
  }

  public OrderType getType() {
    return type;
  }

  public long getQuantity() {
    return quantity;
  }

  public BigDecimal getLimitPrice() {
    return limitPrice;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public OrderStatus getStatus() {
    return status;
  }
}

