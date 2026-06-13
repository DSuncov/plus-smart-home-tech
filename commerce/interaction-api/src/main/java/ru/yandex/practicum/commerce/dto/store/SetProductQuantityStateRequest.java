package ru.yandex.practicum.commerce.dto.store;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record SetProductQuantityStateRequest(
    @NotBlank UUID productId,
    @NotBlank String quantityState
) {}
