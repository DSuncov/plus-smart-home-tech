package ru.yandex.practicum.commerce.dto.delivery;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ShippedRequest(
    @NotNull UUID orderId,
    @NotNull UUID deliveryId
) {}
