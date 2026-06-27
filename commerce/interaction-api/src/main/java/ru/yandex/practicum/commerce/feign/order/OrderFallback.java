package ru.yandex.practicum.commerce.feign.order;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.commerce.dto.order.CreatedNewOrderRequest;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public class OrderFallback implements OrderClient {

    @Override
    public ResponseEntity<List<OrderDto>> getOrdersByUser(String username) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<OrderDto> createOrder(CreatedNewOrderRequest request) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<OrderDto> returnOrder(ProductReturnRequest request) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<OrderDto> successPayment(UUID orderId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<OrderDto> failedPayment(UUID orderId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<OrderDto> successDeliveryOrder(UUID orderId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<OrderDto> failedDeliveryOrder(UUID orderId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<OrderDto> completedOrder(UUID orderId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<OrderDto> calculateTotalOrderCost(UUID orderId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<OrderDto> calculateDeliveryCost(UUID orderId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<OrderDto> successAssembly(UUID orderId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<OrderDto> failedAssembly(UUID orderId) {
        throw new RuntimeException("Сервис недоступен.");
    }
}
