package com.example.ordermanagement.application.usecase;

import com.example.ordermanagement.application.port.in.CreateOrderUseCase;
import com.example.ordermanagement.application.port.out.OrderRepository;
import com.example.ordermanagement.domain.Order;

public final class CreateOrderService implements CreateOrderUseCase {

  private final OrderRepository orderRepository;

  public CreateOrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public Order execute(Command command) {
    var order =
        Order.createNew(
            command.symbol(), command.side(), command.type(), command.quantity(), command.limitPrice());
    return orderRepository.save(order);
  }
}

