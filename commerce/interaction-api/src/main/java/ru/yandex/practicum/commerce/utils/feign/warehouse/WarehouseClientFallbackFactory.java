package ru.yandex.practicum.commerce.utils.feign.warehouse;

import org.springframework.cloud.openfeign.FallbackFactory;

public class WarehouseClientFallbackFactory implements FallbackFactory<WarehouseClient> {
    @Override
    public WarehouseClient create(Throwable cause) {
        if (cause instanceof RuntimeException) {
            return new WarehouseClientFallback();
        }

        return null;
    }
}
