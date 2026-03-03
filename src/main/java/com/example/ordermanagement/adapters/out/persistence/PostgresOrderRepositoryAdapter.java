package com.example.ordermanagement.adapters.out.persistence;

import com.example.ordermanagement.application.port.out.OrderRepository;
import com.example.ordermanagement.domain.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresOrderRepositoryAdapter implements OrderRepository {

  private final SpringDataOrderRepository springDataOrderRepository;

  public PostgresOrderRepositoryAdapter(SpringDataOrderRepository springDataOrderRepository) {
    this.springDataOrderRepository = springDataOrderRepository;
  }

  @Override
  public Order save(Order order) {
    var saved = springDataOrderRepository.save(toEntity(order));
    return toDomain(saved);
  }

  @Override
  public Optional<Order> findById(UUID id) {
    return springDataOrderRepository.findById(id).map(this::toDomain);
  }

  @Override
  public List<Order> findAll() {
    return springDataOrderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
        .map(this::toDomain)
        .toList();
  }

  private Order toDomain(OrderJpaEntity entity) {
    return Order.restore(
        entity.getId(),
        entity.getSymbol(),
        entity.getSide(),
        entity.getType(),
        entity.getQuantity(),
        entity.getLimitPrice(),
        entity.getCreatedAt(),
        entity.getStatus());
  }

  private OrderJpaEntity toEntity(Order order) {
    return new OrderJpaEntity(
        order.id(),
        order.symbol(),
        order.side(),
        order.type(),
        order.quantity(),
        order.limitPrice(),
        order.createdAt(),
        order.status());
  }
}

