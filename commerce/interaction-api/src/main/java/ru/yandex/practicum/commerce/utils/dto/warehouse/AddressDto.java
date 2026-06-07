package ru.yandex.practicum.commerce.utils.dto.warehouse;

public record AddressDto(
    String country,
    String city,
    String street,
    String house,
    String flat
) {}
