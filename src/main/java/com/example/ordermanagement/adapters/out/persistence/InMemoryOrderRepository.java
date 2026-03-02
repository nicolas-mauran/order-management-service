package com.example.ordermanagement.adapters.out.persistence;

import com.example.ordermanagement.application.port.out.OrderRepository;
import com.example.ordermanagement.domain.Order;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOrderRepository implements OrderRepository {

  private final Map<UUID, Order> orders = new ConcurrentHashMap<>();

  @Override
  public Order save(Order order) {
    orders.put(order.id(), order);
    return order;
  }

  @Override
  public Optional<Order> findById(UUID id) {
    return Optional.ofNullable(orders.get(id));
  }
}

