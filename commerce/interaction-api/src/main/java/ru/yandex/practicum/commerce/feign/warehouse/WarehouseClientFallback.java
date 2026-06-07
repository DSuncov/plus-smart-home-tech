package ru.yandex.practicum.commerce.feign.warehouse;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.warehouse.ProductDto;

import java.util.Map;
import java.util.UUID;

public class WarehouseClientFallback implements WarehouseClient{

    @Override
    public void addProductToWarehouse(ProductDto product) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<BookedProductsDto> checkQuantityProductInWarehouse(Map<UUID, Long> products) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public void addProductQuantityToWarehouse(AddProductToWarehouseRequest request) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<AddressDto> getWarehouseAddress() {
        throw new RuntimeException("Сервис недоступен.");
    }
}
