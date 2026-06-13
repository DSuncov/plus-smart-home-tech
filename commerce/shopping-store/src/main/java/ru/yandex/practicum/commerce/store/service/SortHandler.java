package ru.yandex.practicum.commerce.store.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.utils.SortObject;

import java.util.ArrayList;
import java.util.List;

@Component
public class SortHandler {

    private static final String DEFAULT_DIRECTION = "asc";

    public List<SortObject> createSortObject(Sort sort) {
        List<SortObject> sortObjects = new ArrayList<>();

        if (sort != null) {
            for (Sort.Order order : sort) {
                String property = order.getProperty();
                Sort.Direction direction = order.getDirection();

                SortObject sortObject = new SortObject(
                        String.valueOf(direction),
                        null,
                        direction.isAscending(),
                        property,
                        false
                );

                sortObjects.add(sortObject);
            }
        } else {
            SortObject sortObject = new SortObject(
                    DEFAULT_DIRECTION,
                    null,
                    true,
                    null,
                    false
            );

            sortObjects.add(sortObject);
        }

        return sortObjects;
    }
}
