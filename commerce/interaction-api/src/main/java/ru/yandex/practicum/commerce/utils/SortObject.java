package ru.yandex.practicum.commerce.utils;

public record SortObject(
        String direction,
        String nullHandling,
        Boolean ascending,
        String property,
        Boolean ignoreCase
) { }
