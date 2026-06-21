package ru.yandex.practicum.commerce.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.dto.order.CreatedNewOrderRequest;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.feign.order.OrderOperations;
import ru.yandex.practicum.commerce.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController implements OrderOperations {

    private final OrderService orderService;

    @Override
    public ResponseEntity<List<OrderDto>> getOrdersByUser(String username) {
        List<OrderDto> result = orderService.getOrdersByUser(username);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> createOrder(CreatedNewOrderRequest request) {
        OrderDto result = orderService.createOrder(request);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> returnOrder(ProductReturnRequest request) {
        OrderDto result = orderService.returnOrder(request);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> successPayment(UUID orderId) {
        OrderDto result = orderService.successPayment(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> failedPayment(UUID orderId) {
        OrderDto result = orderService.failedPayment(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> successDeliveryOrder(UUID orderId) {
        OrderDto result = orderService.successDeliveryOrder(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> failedDeliveryOrder(UUID orderId) {
        OrderDto result = orderService.failedDeliveryOrder(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> completedOrder(UUID orderId) {
        OrderDto result = orderService.completedOrder(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> calculateTotalOrderCost(UUID orderId) {
        OrderDto result = orderService.calculateTotalOrderCost(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> calculateDeliveryCost(UUID orderId) {
        OrderDto result = orderService.calculateDeliveryCost(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> successAssembly(UUID orderId) {
        OrderDto result = orderService.successAssembly(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> failedAssembly(UUID orderId) {
        OrderDto result = orderService.failedAssembly(orderId);
        return ResponseEntity.ok(result);
    }
}
