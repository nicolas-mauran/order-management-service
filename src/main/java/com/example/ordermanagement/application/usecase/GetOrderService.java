package com.example.ordermanagement.application.usecase;

import com.example.ordermanagement.application.port.in.GetOrderUseCase;
import com.example.ordermanagement.application.port.out.OrderRepository;
import com.example.ordermanagement.domain.Order;
import java.util.Optional;
import java.util.UUID;

public final class GetOrderService implements GetOrderUseCase {

  private final OrderRepository orderRepository;

  public GetOrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public Optional<Order> execute(UUID orderId) {
    return orderRepository.findById(orderId);
  }
}

