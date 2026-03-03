package com.example.ordermanagement.application.usecase;

import com.example.ordermanagement.application.port.in.FillOrderUseCase;
import com.example.ordermanagement.application.port.out.OrderRepository;
import com.example.ordermanagement.domain.Order;
import java.util.UUID;

public final class FillOrderService implements FillOrderUseCase {

  private final OrderRepository orderRepository;

  public FillOrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public Order execute(UUID orderId) {
    var order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    order.fill();
    return orderRepository.save(order);
  }
}
