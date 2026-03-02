package com.example.ordermanagement.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.ordermanagement.adapters.in.web.OrderController;
import com.example.ordermanagement.adapters.in.web.RestExceptionHandler;
import com.example.ordermanagement.application.port.in.CreateOrderUseCase;
import com.example.ordermanagement.application.port.in.GetOrderUseCase;
import com.example.ordermanagement.domain.Order;
import com.example.ordermanagement.domain.OrderSide;
import com.example.ordermanagement.domain.OrderStatus;
import com.example.ordermanagement.domain.OrderType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = OrderController.class)
@Import(RestExceptionHandler.class)
class OrderControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CreateOrderUseCase createOrderUseCase;

  @MockBean private GetOrderUseCase getOrderUseCase;

  @Test
  void should_create_order() throws Exception {
    var orderId = UUID.fromString("9d5f41f4-f60e-4d43-a04c-5515c55fd252");
    var created =
        Order.restore(
            orderId,
            "AAPL",
            OrderSide.BUY,
            OrderType.MARKET,
            10,
            null,
            Instant.parse("2026-03-02T16:20:00Z"),
            OrderStatus.NEW);
    when(createOrderUseCase.execute(any())).thenReturn(created);

    mockMvc
        .perform(
            post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "symbol": "aapl",
                      "side": "BUY",
                      "type": "MARKET",
                      "quantity": 10
                    }
                    """))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/orders/" + orderId))
        .andExpect(jsonPath("$.id").value(orderId.toString()))
        .andExpect(jsonPath("$.symbol").value("AAPL"))
        .andExpect(jsonPath("$.status").value("NEW"));
  }

  @Test
  void should_get_order_by_id() throws Exception {
    var orderId = UUID.fromString("d4d21d9c-b2a0-48f6-b2d0-63f8a7db8bde");
    var order =
        Order.restore(
            orderId,
            "MSFT",
            OrderSide.SELL,
            OrderType.LIMIT,
            20,
            new BigDecimal("250.50"),
            Instant.parse("2026-03-02T16:20:00Z"),
            OrderStatus.NEW);
    when(getOrderUseCase.execute(orderId)).thenReturn(Optional.of(order));

    mockMvc
        .perform(get("/orders/{id}", orderId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(orderId.toString()))
        .andExpect(jsonPath("$.symbol").value("MSFT"))
        .andExpect(jsonPath("$.type").value("LIMIT"))
        .andExpect(jsonPath("$.limitPrice").value(250.50));
  }

  @Test
  void should_return_404_when_order_not_found() throws Exception {
    var orderId = UUID.fromString("6d3f40db-9f05-4a9d-9226-e7f1002f9949");
    when(getOrderUseCase.execute(orderId)).thenReturn(Optional.empty());

    mockMvc.perform(get("/orders/{id}", orderId)).andExpect(status().isNotFound());
  }

  @Test
  void should_return_400_when_validation_fails() throws Exception {
    mockMvc
        .perform(
            post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "symbol": "",
                      "side": "BUY",
                      "type": "MARKET",
                      "quantity": 0
                    }
                    """))
        .andExpect(status().isBadRequest());
  }
}

