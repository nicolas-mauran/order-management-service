package com.example.ordermanagement.application.port.in;

import com.example.ordermanagement.domain.Order;
import java.util.Optional;
import java.util.UUID;

public interface GetOrderUseCase {
  Optional<Order> execute(UUID orderId);
}

