package com.example.ordermanagement.application.usecase;

import com.example.ordermanagement.application.port.in.ListOrdersUseCase;
import com.example.ordermanagement.application.port.out.OrderRepository;
import com.example.ordermanagement.domain.Order;
import java.util.List;

public final class ListOrdersService implements ListOrdersUseCase {

  private final OrderRepository orderRepository;

  public ListOrdersService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public List<Order> execute() {
    return orderRepository.findAll();
  }
}

