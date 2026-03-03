package com.example.ordermanagement.application.usecase;

import com.example.ordermanagement.application.port.in.CancelOrderUseCase;
import com.example.ordermanagement.application.port.out.OrderRepository;
import com.example.ordermanagement.domain.Order;
import java.util.UUID;

public final class CancelOrderService implements CancelOrderUseCase {

  private final OrderRepository orderRepository;

  public CancelOrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public Order execute(UUID orderId) {
    var order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    order.cancel();
    return orderRepository.save(order);
  }
}

