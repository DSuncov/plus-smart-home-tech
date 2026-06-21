package ru.yandex.practicum.commerce.feign.payment;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class PaymentFallback implements PaymentClient {
    @Override
    public ResponseEntity<PaymentDto> createPayment(OrderDto request) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<BigDecimal> calculateTotalCost(OrderDto request) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public void refundPayment(UUID paymentId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<BigDecimal> calculateProductCost(Map<UUID, Long> products) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public void failedPayment(UUID paymentId) {
        throw new RuntimeException("Сервис недоступен.");
    }
}
