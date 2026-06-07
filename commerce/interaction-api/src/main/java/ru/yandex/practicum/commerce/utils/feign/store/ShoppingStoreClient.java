package ru.yandex.practicum.commerce.utils.feign.store;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store",
        fallback = ShoppingStoreClientFallbackFactory.class, fallbackFactory = ShoppingStoreClientFallbackFactory.class)
public interface ShoppingStoreClient extends ShoppingStoreOperations {
}
