package ru.yandex.practicum.commerce.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.utils.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.utils.feign.store.ShoppingStoreClient;
import ru.yandex.practicum.commerce.utils.feign.warehouse.WarehouseClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShoppingCartExternalServiceClient {

    private final WarehouseClient warehouseClient;
    private final ShoppingStoreClient shoppingStoreClient;

    public void isEnoughQuantity(ShoppingCartDto cart) {
        warehouseClient.checkQuantityProductInWarehouse(cart).getBody();
    }
}
