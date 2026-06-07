package ru.yandex.practicum.commerce.dto.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;
import java.util.UUID;

public record ShoppingCartDto(
    @NotBlank UUID shoppingCartId,
    @NotEmpty Map<UUID, Long> products
) {}
