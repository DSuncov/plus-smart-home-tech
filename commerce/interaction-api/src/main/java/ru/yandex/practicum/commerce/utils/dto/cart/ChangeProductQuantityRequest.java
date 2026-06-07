package ru.yandex.practicum.commerce.utils.dto.cart;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ChangeProductQuantityRequest(
    @NotBlank UUID productId,
    @NotBlank Long newQuantity
) {}
