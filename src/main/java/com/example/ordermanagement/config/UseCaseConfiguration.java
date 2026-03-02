package com.example.ordermanagement.config;

import com.example.ordermanagement.adapters.out.persistence.InMemoryOrderRepository;
import com.example.ordermanagement.application.port.in.CreateOrderUseCase;
import com.example.ordermanagement.application.port.in.GetOrderUseCase;
import com.example.ordermanagement.application.port.out.OrderRepository;
import com.example.ordermanagement.application.usecase.CreateOrderService;
import com.example.ordermanagement.application.usecase.GetOrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

  @Bean
  public OrderRepository orderRepository() {
    return new InMemoryOrderRepository();
  }

  @Bean
  public CreateOrderUseCase createOrderUseCase(OrderRepository orderRepository) {
    return new CreateOrderService(orderRepository);
  }

  @Bean
  public GetOrderUseCase getOrderUseCase(OrderRepository orderRepository) {
    return new GetOrderService(orderRepository);
  }
}

