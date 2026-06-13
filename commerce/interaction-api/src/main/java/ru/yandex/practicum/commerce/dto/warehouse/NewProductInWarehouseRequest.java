package ru.yandex.practicum.commerce.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NewProductInWarehouseRequest(
    @NotBlank UUID productId,
    Boolean fragile,
    @NotNull DimensionDto dimension,
    @NotNull @Min(1) Double weight
) {}
