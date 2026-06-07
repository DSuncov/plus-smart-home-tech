package ru.yandex.practicum.commerce.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookedProductsDto(
    @NotNull @Positive Double deliveryWeight,
    @NotNull @Positive Double deliveryVolume,
    @NotNull Boolean fragile
) {}
