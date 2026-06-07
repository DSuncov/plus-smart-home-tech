package ru.yandex.practicum.commerce.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.utils.feign.warehouse.WarehouseClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingStoreExternalServiceClient {

    private final WarehouseClient warehouseClient;
}
