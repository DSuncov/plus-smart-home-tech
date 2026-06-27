package ru.yandex.practicum.commerce.feign.order;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order", path = "/api/v1/order",
        fallback = OrderFallback.class, fallbackFactory = OrderClientFallbackFactory.class)
public interface OrderClient extends OrderOperations {
}
