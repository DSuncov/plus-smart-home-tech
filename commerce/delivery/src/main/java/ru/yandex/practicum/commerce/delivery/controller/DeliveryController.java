package ru.yandex.practicum.commerce.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.delivery.service.DeliveryService;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.feign.delivery.DeliveryOperations;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/delivery")
public class DeliveryController implements DeliveryOperations {

    private final DeliveryService service;

    @Override
    public ResponseEntity<DeliveryDto> createNewDelivery(DeliveryDto request) {
        DeliveryDto response = service.createNewDelivery(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public void successfulDelivery(UUID orderId) {
        service.successfulDelivery(orderId);
    }

    @Override
    public void pickedDelivery(UUID orderId) {
        service.pickedDelivery(orderId);
    }

    @Override
    public void failedDelivery(UUID orderId) {
        service.deliveryFailed(orderId);
    }

    @Override
    public BigDecimal deliveryCost(OrderDto request) {
        return service.calculateDeliveryTotalCost(request);
    }
}
