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
import com.example.ordermanagement.application.port.in.CancelOrderUseCase;
import com.example.ordermanagement.application.port.in.CreateOrderUseCase;
import com.example.ordermanagement.application.port.in.FillOrderUseCase;
import com.example.ordermanagement.application.port.in.GetOrderUseCase;
import com.example.ordermanagement.application.port.in.ListOrdersUseCase;
import com.example.ordermanagement.application.usecase.OrderNotFoundException;
import com.example.ordermanagement.domain.Order;
import com.example.ordermanagement.domain.OrderSide;
import com.example.ordermanagement.domain.OrderStatus;
import com.example.ordermanagement.domain.OrderType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
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

  @MockBean private ListOrdersUseCase listOrdersUseCase;

  @MockBean private CancelOrderUseCase cancelOrderUseCase;

  @MockBean private FillOrderUseCase fillOrderUseCase;

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

  @Test
  void should_list_orders() throws Exception {
    var firstId = UUID.fromString("28cd1d4f-3d20-4d31-8552-f247dd7f3ce4");
    var secondId = UUID.fromString("9d27e4f3-0e31-4d5f-bf17-16ce2bf93a11");
    when(listOrdersUseCase.execute())
        .thenReturn(
            List.of(
                Order.restore(
                    secondId,
                    "MSFT",
                    OrderSide.SELL,
                    OrderType.LIMIT,
                    5,
                    new BigDecimal("200.00"),
                    Instant.parse("2026-03-02T16:23:00Z"),
                    OrderStatus.NEW),
                Order.restore(
                    firstId,
                    "AAPL",
                    OrderSide.BUY,
                    OrderType.MARKET,
                    10,
                    null,
                    Instant.parse("2026-03-02T16:22:00Z"),
                    OrderStatus.NEW)));

    mockMvc
        .perform(get("/orders"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(secondId.toString()))
        .andExpect(jsonPath("$[1].id").value(firstId.toString()));
  }

  @Test
  void should_cancel_order() throws Exception {
    var orderId = UUID.fromString("3f5ca8fa-8e7d-4f4f-a5b2-7ea09be4d20c");
    var cancelled =
        Order.restore(
            orderId,
            "AAPL",
            OrderSide.BUY,
            OrderType.MARKET,
            10,
            null,
            Instant.parse("2026-03-02T16:20:00Z"),
            OrderStatus.CANCELLED);
    when(cancelOrderUseCase.execute(orderId)).thenReturn(cancelled);

    mockMvc
        .perform(post("/orders/{id}/cancel", orderId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(orderId.toString()))
        .andExpect(jsonPath("$.status").value("CANCELLED"));
  }

  @Test
  void should_return_404_when_cancelling_unknown_order() throws Exception {
    var orderId = UUID.fromString("da4b6366-bbe4-4450-a2c7-3f4af0ad8e30");
    when(cancelOrderUseCase.execute(orderId)).thenThrow(new OrderNotFoundException(orderId));

    mockMvc.perform(post("/orders/{id}/cancel", orderId)).andExpect(status().isNotFound());
  }

  @Test
  void should_fill_order() throws Exception {
    var orderId = UUID.fromString("47811163-2f59-4f65-bbc6-e4a49d1d7b33");
    var filled =
        Order.restore(
            orderId,
            "AAPL",
            OrderSide.BUY,
            OrderType.MARKET,
            10,
            null,
            Instant.parse("2026-03-02T16:20:00Z"),
            OrderStatus.FILLED);
    when(fillOrderUseCase.execute(orderId)).thenReturn(filled);

    mockMvc
        .perform(post("/orders/{id}/fill", orderId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(orderId.toString()))
        .andExpect(jsonPath("$.status").value("FILLED"));
  }

  @Test
  void should_return_404_when_filling_unknown_order() throws Exception {
    var orderId = UUID.fromString("838fcf81-5d79-4c4f-b617-62f45f8e2fce");
    when(fillOrderUseCase.execute(orderId)).thenThrow(new OrderNotFoundException(orderId));

    mockMvc.perform(post("/orders/{id}/fill", orderId)).andExpect(status().isNotFound());
  }
}
