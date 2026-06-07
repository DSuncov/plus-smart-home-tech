package ru.yandex.practicum.commerce.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.feign.warehouse.WarehouseClient;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartExternalService {

    private final WarehouseClient warehouseClient;

    public void checkQuantity(Map<UUID, Long> products) {
        warehouseClient.checkQuantityProductInWarehouse(products);
    }
}
