package ru.yandex.practicum.commerce.dto.warehouse;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record AssemblyProductsForOrderRequest(
        @NotNull UUID orderId,
        @NotNull @NotEmpty Map<UUID, Long> products
) {}
