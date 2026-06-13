package ru.yandex.practicum.commerce.utils;

import java.util.List;

public record PageableObject (
        Long offset,
        List<SortObject> sort,
        Boolean unpaged,
        Boolean paged,
        Integer pageNumber,
        Integer pageSize
) {}
