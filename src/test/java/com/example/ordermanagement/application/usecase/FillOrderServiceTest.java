package com.example.ordermanagement.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.ordermanagement.application.port.out.OrderRepository;
import com.example.ordermanagement.domain.Order;
import com.example.ordermanagement.domain.OrderSide;
import com.example.ordermanagement.domain.OrderStatus;
import com.example.ordermanagement.domain.OrderType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FillOrderServiceTest {

  @Mock private OrderRepository orderRepository;

  @Test
  void should_fill_existing_new_order() {
    var orderId = UUID.fromString("6a06790f-0f4b-44f1-a632-264abf8bc111");
    var order =
        Order.restore(
            orderId,
            "AAPL",
            OrderSide.BUY,
            OrderType.MARKET,
            10,
            null,
            Instant.parse("2026-03-02T16:20:00Z"),
            OrderStatus.NEW);
    when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
    when(orderRepository.save(order)).thenReturn(order);

    var service = new FillOrderService(orderRepository);
    var updated = service.execute(orderId);

    assertThat(updated.status()).isEqualTo(OrderStatus.FILLED);
    verify(orderRepository).save(order);
  }

  @Test
  void should_throw_not_found_when_order_does_not_exist() {
    var orderId = UUID.fromString("953e2dff-e2ac-4fe8-96e2-334436a13bb2");
    when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

    var service = new FillOrderService(orderRepository);

    assertThatThrownBy(() -> service.execute(orderId))
        .isInstanceOf(OrderNotFoundException.class)
        .hasMessageContaining(orderId.toString());
  }

  @Test
  void should_reject_fill_when_order_is_not_new() {
    var orderId = UUID.fromString("3490f324-c87b-44ee-ab45-a9b86c29fce2");
    var order =
        Order.restore(
            orderId,
            "MSFT",
            OrderSide.SELL,
            OrderType.LIMIT,
            5,
            new BigDecimal("250.50"),
            Instant.parse("2026-03-02T16:20:00Z"),
            OrderStatus.CANCELLED);
    when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

    var service = new FillOrderService(orderRepository);

    assertThatThrownBy(() -> service.execute(orderId))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Only NEW orders can be filled");
  }
}
