package com.example.ordermanagement.adapters.in.web;

import com.example.ordermanagement.domain.OrderSide;
import com.example.ordermanagement.domain.OrderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateOrderRequest(
    @NotBlank String symbol,
    @NotNull OrderSide side,
    @NotNull OrderType type,
    @Positive long quantity,
    BigDecimal limitPrice) {}

