package ru.yandex.practicum.commerce.utils.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductDto(
    @NotNull UUID productId,
    @NotNull Boolean fragile,
    @NotNull DimensionDto dimension,
    @NotNull @Min(1) Double weight
) {}
