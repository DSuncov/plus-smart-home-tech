package ru.yandex.practicum.commerce.utils.feign.warehouse;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse",
        fallback = WarehouseClientFallback.class, fallbackFactory = WarehouseClientFallbackFactory.class)
public interface WarehouseClient extends WarehouseOperations {
}
