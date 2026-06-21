package ru.yandex.practicum.commerce.dto.order;

import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;

public record CreatedNewOrderRequest(
    @NotNull ShoppingCartDto shoppingCart,
    @NotNull AddressDto deliveryAddress
) {}
