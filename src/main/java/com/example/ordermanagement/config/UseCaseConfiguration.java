package com.example.ordermanagement.config;

import com.example.ordermanagement.application.port.in.CancelOrderUseCase;
import com.example.ordermanagement.application.port.in.CreateOrderUseCase;
import com.example.ordermanagement.application.port.in.FillOrderUseCase;
import com.example.ordermanagement.application.port.in.GetOrderUseCase;
import com.example.ordermanagement.application.port.in.ListOrdersUseCase;
import com.example.ordermanagement.application.port.out.OrderRepository;
import com.example.ordermanagement.application.usecase.CancelOrderService;
import com.example.ordermanagement.application.usecase.CreateOrderService;
import com.example.ordermanagement.application.usecase.FillOrderService;
import com.example.ordermanagement.application.usecase.GetOrderService;
import com.example.ordermanagement.application.usecase.ListOrdersService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

  @Bean
  public CreateOrderUseCase createOrderUseCase(OrderRepository orderRepository) {
    return new CreateOrderService(orderRepository);
  }

  @Bean
  public GetOrderUseCase getOrderUseCase(OrderRepository orderRepository) {
    return new GetOrderService(orderRepository);
  }

  @Bean
  public ListOrdersUseCase listOrdersUseCase(OrderRepository orderRepository) {
    return new ListOrdersService(orderRepository);
  }

  @Bean
  public CancelOrderUseCase cancelOrderUseCase(OrderRepository orderRepository) {
    return new CancelOrderService(orderRepository);
  }

  @Bean
  public FillOrderUseCase fillOrderUseCase(OrderRepository orderRepository) {
    return new FillOrderService(orderRepository);
  }
}
