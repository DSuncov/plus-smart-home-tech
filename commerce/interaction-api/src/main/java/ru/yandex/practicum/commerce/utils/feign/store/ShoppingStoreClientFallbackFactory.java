package ru.yandex.practicum.commerce.utils.feign.store;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ShoppingStoreClientFallbackFactory implements FallbackFactory<ShoppingStoreClient> {
    @Override
    public ShoppingStoreClient create(Throwable cause) {
        if (cause instanceof RuntimeException) {
            return new ShoppingStoreFallback();
        }

        return null;
    }
}
