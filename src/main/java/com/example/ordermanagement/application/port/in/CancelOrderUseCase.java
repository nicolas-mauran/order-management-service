package com.example.ordermanagement.application.port.in;

import com.example.ordermanagement.domain.Order;
import java.util.UUID;

public interface CancelOrderUseCase {
  Order execute(UUID orderId);
}

