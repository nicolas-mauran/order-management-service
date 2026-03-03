package com.example.ordermanagement.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.ordermanagement.adapters.in.web.ApiError;
import com.example.ordermanagement.adapters.in.web.OrderResponse;
import com.example.ordermanagement.adapters.out.persistence.SpringDataOrderRepository;
import com.example.ordermanagement.domain.OrderStatus;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
class OrderApiIntegrationTest {

  @Container
  static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
      new PostgreSQLContainer<>("docker.io/library/postgres:16");

  @DynamicPropertySource
  static void configureDatasource(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
  }

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private SpringDataOrderRepository springDataOrderRepository;

  @BeforeEach
  void cleanDatabase() {
    springDataOrderRepository.deleteAll();
  }

  @Test
  void should_create_then_get_order() {
    var createResponse =
        restTemplate.postForEntity(
            "/orders",
            new CreateOrderPayload("aapl", "BUY", "MARKET", 10, null),
            OrderResponse.class);

    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(createResponse.getBody()).isNotNull();
    assertThat(createResponse.getBody().symbol()).isEqualTo("AAPL");
    assertThat(createResponse.getBody().status()).isEqualTo(OrderStatus.NEW);

    UUID orderId = createResponse.getBody().id();
    var getResponse = restTemplate.getForEntity("/orders/{id}", OrderResponse.class, orderId);

    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(getResponse.getBody()).isNotNull();
    assertThat(getResponse.getBody().id()).isEqualTo(orderId);
    assertThat(getResponse.getBody().symbol()).isEqualTo("AAPL");
  }

  @Test
  void should_list_and_cancel_order() {
    var firstOrderId =
        createOrderAndReturnId(new CreateOrderPayload("AAPL", "BUY", "MARKET", 10, null));
    var secondOrderId =
        createOrderAndReturnId(new CreateOrderPayload("MSFT", "SELL", "LIMIT", 20, new BigDecimal("250.50")));

    var listResponse = restTemplate.getForEntity("/orders", OrderResponse[].class);

    assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(listResponse.getBody()).isNotNull();
    assertThat(listResponse.getBody()).hasSize(2);
    assertThat(Arrays.stream(listResponse.getBody()).map(OrderResponse::id).toList())
        .containsExactlyInAnyOrder(firstOrderId, secondOrderId);

    var cancelResponse =
        restTemplate.postForEntity("/orders/{id}/cancel", null, OrderResponse.class, firstOrderId);

    assertThat(cancelResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(cancelResponse.getBody()).isNotNull();
    assertThat(cancelResponse.getBody().status()).isEqualTo(OrderStatus.CANCELLED);

    ResponseEntity<ApiError> cancelAgainResponse =
        restTemplate.postForEntity("/orders/{id}/cancel", null, ApiError.class, firstOrderId);

    assertThat(cancelAgainResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(cancelAgainResponse.getBody()).isNotNull();
    assertThat(cancelAgainResponse.getBody().message()).contains("Only NEW orders can be cancelled");

    var fillResponse =
        restTemplate.postForEntity("/orders/{id}/fill", null, OrderResponse.class, secondOrderId);

    assertThat(fillResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(fillResponse.getBody()).isNotNull();
    assertThat(fillResponse.getBody().status()).isEqualTo(OrderStatus.FILLED);

    ResponseEntity<ApiError> fillAgainResponse =
        restTemplate.postForEntity("/orders/{id}/fill", null, ApiError.class, secondOrderId);

    assertThat(fillAgainResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(fillAgainResponse.getBody()).isNotNull();
    assertThat(fillAgainResponse.getBody().message()).contains("Only NEW orders can be filled");
  }

  @Test
  void should_return_not_found_when_cancelling_unknown_order() {
    UUID unknownId = UUID.fromString("67a14672-5727-4c34-9ca9-aef45cb81ef5");

    var response = restTemplate.postForEntity("/orders/{id}/cancel", null, ApiError.class, unknownId);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().message()).contains("Order not found");
  }

  @Test
  void should_return_not_found_when_filling_unknown_order() {
    UUID unknownId = UUID.fromString("c777e4da-bb9c-4f38-bf96-3e77ca3ce4dd");

    var response = restTemplate.postForEntity("/orders/{id}/fill", null, ApiError.class, unknownId);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().message()).contains("Order not found");
  }

  private UUID createOrderAndReturnId(CreateOrderPayload payload) {
    var response = restTemplate.postForEntity("/orders", payload, OrderResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    return response.getBody().id();
  }

  private record CreateOrderPayload(
      String symbol, String side, String type, long quantity, BigDecimal limitPrice) {}
}
