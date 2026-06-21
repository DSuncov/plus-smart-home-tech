package ru.yandex.practicum.commerce.feign.delivery;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryOperations {

    @PutMapping
    ResponseEntity<DeliveryDto> createNewDelivery(@RequestBody @NotNull @Valid DeliveryDto request);

    @PostMapping("/successful")
    void successfulDelivery(@RequestBody @NotNull UUID orderId);

    @PostMapping("/picked")
    void pickedDelivery(@RequestBody @NotNull UUID orderId);

    @PostMapping("/failed")
    void failedDelivery(@RequestBody @NotNull UUID orderId);

    @PostMapping("/cost")
    BigDecimal deliveryCost(@RequestBody @NotNull @Valid OrderDto request);
}
