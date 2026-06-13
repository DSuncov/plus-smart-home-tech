package ru.yandex.practicum.commerce.feign.cart;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart",
        fallback = ShoppingCartFallback.class, fallbackFactory = ShoppingCartClientFallbackFactory.class)
public interface ShoppingCartClient extends ShoppingCartOperations {
}
