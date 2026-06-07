package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.utils.feign.cart.ShoppingCartClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseExternalServiceClient {

    private final ShoppingCartClient shoppingCartClient;

}
