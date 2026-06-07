package ru.yandex.practicum.commerce.warehouse.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.utils.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.utils.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.utils.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.utils.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.utils.dto.warehouse.ProductDto;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseDatabaseService;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseExternalServiceClient;

import java.security.SecureRandom;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseOrchestrationService {

    private final WarehouseDatabaseService databaseService;
    private final WarehouseExternalServiceClient externalServiceClient;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    public void addProductToWarehouse(ProductDto product) {
        log.info("Добавялем новый товар на склад.");
        databaseService.addProductToWarehouse(product);
        log.info("Товар добавлен на склад.");
    }

    public BookedProductsDto checkQuantityProductInWarehouse(ShoppingCartDto cart) {
        log.info("Отправляем запрос на склад для уточнения количества доступных товаров.");
        BookedProductsDto bookedProductsDto = databaseService.checkQuantityProductInWarehouse(cart.products());
        log.info("Товары доступны для заказа.");
        return bookedProductsDto;
    }

    public void addProductQuantityToWarehouse(AddProductToWarehouseRequest request) {
        log.info("Отправляем запрос на обновление количества товара на складе.");
        databaseService.addProductQuantityToWarehouse(request);
        log.info("Количество товара на складе обновлено.");
    }

    public AddressDto getWarehouseAddress() {
        log.info("Отправляем запрос на получение адреса склада.");
        AddressDto addressDto = new AddressDto(
                CURRENT_ADDRESS,
                CURRENT_ADDRESS,
                CURRENT_ADDRESS,
                CURRENT_ADDRESS,
                CURRENT_ADDRESS);

        log.info("Информация об адресе склада получена.");
        return addressDto;
    }
}
