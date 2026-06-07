package ru.yandex.practicum.commerce.utils.dto.store;

import ru.yandex.practicum.commerce.utils.PageableObject;
import ru.yandex.practicum.commerce.utils.SortObject;

public record PageProductDto(
    Double totalElements,
    Integer totalPages,
    Boolean first,
    Boolean last,
    Integer size,
    ProductDto content,
    Integer number,
    SortObject sort,
    Integer numberOfElements,
    PageableObject pageable,
    Boolean empty
) {}
