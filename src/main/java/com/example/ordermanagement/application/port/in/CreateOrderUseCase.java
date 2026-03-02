package com.example.ordermanagement.application.port.in;

import com.example.ordermanagement.domain.Order;
import com.example.ordermanagement.domain.OrderSide;
import com.example.ordermanagement.domain.OrderType;
import java.math.BigDecimal;

public interface CreateOrderUseCase {

  Order execute(Command command);

  record Command(String symbol, OrderSide side, OrderType type, long quantity, BigDecimal limitPrice) {}
}

