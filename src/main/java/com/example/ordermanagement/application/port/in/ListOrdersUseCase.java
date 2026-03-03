package com.example.ordermanagement.application.port.in;

import com.example.ordermanagement.domain.Order;
import java.util.List;

public interface ListOrdersUseCase {
  List<Order> execute();
}

