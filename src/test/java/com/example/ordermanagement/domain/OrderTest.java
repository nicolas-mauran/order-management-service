package com.example.ordermanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  void should_create_market_order_with_null_limit_price() {
    var order = Order.createNew("aapl", OrderSide.BUY, OrderType.MARKET, 10, null);

    assertThat(order.symbol()).isEqualTo("AAPL");
    assertThat(order.status()).isEqualTo(OrderStatus.NEW);
    assertThat(order.limitPrice()).isNull();
  }

  @Test
  void should_reject_market_order_with_limit_price() {
    assertThatThrownBy(
            () ->
                Order.createNew(
                    "AAPL", OrderSide.BUY, OrderType.MARKET, 10, new BigDecimal("100.00")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("limitPrice must be null");
  }

  @Test
  void should_reject_limit_order_without_limit_price() {
    assertThatThrownBy(
            () -> Order.createNew("AAPL", OrderSide.SELL, OrderType.LIMIT, 10, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("limitPrice is required");
  }

  @Test
  void should_reject_limit_order_with_non_positive_price() {
    assertThatThrownBy(
            () ->
                Order.createNew("AAPL", OrderSide.SELL, OrderType.LIMIT, 10, BigDecimal.ZERO))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("limitPrice must be > 0");
  }

  @Test
  void should_cancel_new_order() {
    var order = Order.createNew("AAPL", OrderSide.BUY, OrderType.MARKET, 10, null);

    order.cancel();

    assertThat(order.status()).isEqualTo(OrderStatus.CANCELLED);
  }

  @Test
  void should_refuse_cancel_for_non_new_order() {
    var order = Order.createNew("AAPL", OrderSide.BUY, OrderType.MARKET, 10, null);
    order.fill();

    assertThatThrownBy(order::cancel)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Only NEW orders can be cancelled");
  }
}

