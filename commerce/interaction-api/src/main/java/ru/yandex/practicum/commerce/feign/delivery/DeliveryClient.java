package ru.yandex.practicum.commerce.feign.delivery;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "delivery", path = "/api/v1/delivery",
        fallback = DeliveryFallback.class, fallbackFactory = DeliveryClientFallbackFactory.class)
public interface DeliveryClient extends DeliveryOperations {
}
