package ru.yandex.practicum.commerce.dto.delivery;

import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.enums.DeliveryState;

import java.util.UUID;

public record DeliveryDto(
    @NotNull UUID deliveryId,
    @NotNull AddressDto addressFrom,
    @NotNull AddressDto addressTo,
    @NotNull UUID orderId,
    @NotNull DeliveryState state
) {}
