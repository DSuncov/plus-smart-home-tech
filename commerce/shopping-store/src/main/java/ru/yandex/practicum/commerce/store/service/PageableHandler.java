package ru.yandex.practicum.commerce.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.utils.PageableObject;
import ru.yandex.practicum.commerce.utils.SortObject;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PageableHandler {

    private final SortHandler sortHandler;

    public PageableObject createPageableObject(Integer page, Integer size, Sort sort) {
        List<SortObject> sortObject = sortHandler.createSortObject(sort);
        return new PageableObject((long) page * size, sortObject, false, true, page, size);
    }
}
