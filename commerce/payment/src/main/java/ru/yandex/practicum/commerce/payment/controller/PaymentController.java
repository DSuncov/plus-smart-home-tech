package ru.yandex.practicum.commerce.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.feign.payment.PaymentOperations;
import ru.yandex.practicum.commerce.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/payment")
public class PaymentController implements PaymentOperations {

    private final PaymentService service;

    @Override
    public ResponseEntity<PaymentDto> createPayment(OrderDto request) {
        PaymentDto response = service.createPayment(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<BigDecimal> calculateTotalCost(OrderDto request) {
        BigDecimal totalCost = service.calculateTotalCost(request);
        return ResponseEntity.ok(totalCost);
    }

    @Override
    public void refundPayment(UUID paymentId) {
        service.paymentSuccess(paymentId);
    }

    @Override
    public ResponseEntity<BigDecimal> calculateProductCost(Map<UUID, Long> products) {
        BigDecimal productsCost = service.calculateProductsCost(products);
        return ResponseEntity.ok(productsCost);
    }

    @Override
    public void failedPayment(UUID paymentId) {
        service.paymentFailed(paymentId);
    }
}
