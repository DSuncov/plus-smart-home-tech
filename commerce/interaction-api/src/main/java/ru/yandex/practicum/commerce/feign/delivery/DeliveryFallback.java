package ru.yandex.practicum.commerce.feign.delivery;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public class DeliveryFallback implements DeliveryClient {
    @Override
    public ResponseEntity<DeliveryDto> createNewDelivery(DeliveryDto request) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public void successfulDelivery(UUID orderId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public void pickedDelivery(UUID orderId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public void failedDelivery(UUID orderId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public BigDecimal deliveryCost(OrderDto request) {
        throw new RuntimeException("Сервис недоступен.");
    }
}
