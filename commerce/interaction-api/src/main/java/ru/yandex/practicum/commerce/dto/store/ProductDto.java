package ru.yandex.practicum.commerce.dto.store;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.commerce.enums.ProductCategory;
import ru.yandex.practicum.commerce.enums.ProductState;
import ru.yandex.practicum.commerce.enums.QuantityState;

import java.util.UUID;

public record ProductDto(
        UUID productId,
        @NotBlank String productName,
        @NotBlank String description,
        String imageSrc,
        @NotNull QuantityState quantityState,
        @NotNull ProductState productState,
        ProductCategory productCategory,
        @NotNull @Min(1) Double price
) {}