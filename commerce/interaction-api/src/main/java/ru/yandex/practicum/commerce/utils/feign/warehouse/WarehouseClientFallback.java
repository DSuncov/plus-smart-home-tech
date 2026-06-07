package ru.yandex.practicum.commerce.utils.feign.warehouse;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.commerce.utils.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.utils.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.utils.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.utils.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.utils.dto.warehouse.ProductDto;

public class WarehouseClientFallback implements WarehouseClient{

    @Override
    public void addProductToWarehouse(ProductDto product) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<BookedProductsDto> checkQuantityProductInWarehouse(ShoppingCartDto cart) {
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
