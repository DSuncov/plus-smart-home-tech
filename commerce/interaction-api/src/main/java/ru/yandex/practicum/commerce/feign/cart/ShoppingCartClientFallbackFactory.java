package ru.yandex.practicum.commerce.feign.cart;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartClientFallbackFactory implements FallbackFactory<ShoppingCartClient> {
    @Override
    public ShoppingCartClient create(Throwable cause) {
        if (cause instanceof RuntimeException) {
            return new ShoppingCartFallback();
        }
        return null;
    }
}
