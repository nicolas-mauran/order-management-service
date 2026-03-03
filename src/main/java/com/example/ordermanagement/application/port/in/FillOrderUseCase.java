package com.example.ordermanagement.application.port.in;

import com.example.ordermanagement.domain.Order;
import java.util.UUID;

public interface FillOrderUseCase {
  Order execute(UUID orderId);
}
