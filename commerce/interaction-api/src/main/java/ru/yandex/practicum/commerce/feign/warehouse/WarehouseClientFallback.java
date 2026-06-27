package ru.yandex.practicum.commerce.feign.warehouse;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.commerce.dto.delivery.ShippedRequest;
import ru.yandex.practicum.commerce.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

public class WarehouseClientFallback implements WarehouseClient{

    @Override
    public void addProductToWarehouse(ProductDto product) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public void shipped(ShippedRequest request) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public void returnToWarehouse(ProductReturnRequest request) {
        throw new RuntimeException("Сервис недоступен.");

    }

    @Override
    public ResponseEntity<BookedProductsDto> checkQuantityProductInWarehouse(Map<UUID, Long> products) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<BookedProductsDto> assemblyProductForOrderFromShoppingCart(AssemblyProductsForOrderRequest request) {
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
