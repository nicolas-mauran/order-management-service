package com.example.ordermanagement.adapters.in.web;

import com.example.ordermanagement.application.port.in.CreateOrderUseCase;
import com.example.ordermanagement.application.port.in.GetOrderUseCase;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final CreateOrderUseCase createOrderUseCase;
  private final GetOrderUseCase getOrderUseCase;

  public OrderController(CreateOrderUseCase createOrderUseCase, GetOrderUseCase getOrderUseCase) {
    this.createOrderUseCase = createOrderUseCase;
    this.getOrderUseCase = getOrderUseCase;
  }

  @PostMapping
  public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
    var command =
        new CreateOrderUseCase.Command(
            request.symbol(), request.side(), request.type(), request.quantity(), request.limitPrice());
    var created = createOrderUseCase.execute(command);
    return ResponseEntity.created(URI.create("/orders/" + created.id())).body(OrderResponse.from(created));
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponse> get(@PathVariable UUID id) {
    return getOrderUseCase.execute(id)
        .map(order -> ResponseEntity.ok(OrderResponse.from(order)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}

