package ru.yandex.practicum.commerce.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.dto.delivery.ShippedRequest;
import ru.yandex.practicum.commerce.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.dto.warehouse.*;
import ru.yandex.practicum.commerce.feign.warehouse.WarehouseOperations;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/warehouse")
public class WarehouseController implements WarehouseOperations {

    private final WarehouseService service;

    @Override
    public void addProductToWarehouse(ProductDto product) {
        service.addProductToWarehouse(product);
    }

    @Override
    public void shipped(ShippedRequest request) {
        service.shippedToDelivery(request);
    }

    @Override
    public void returnToWarehouse(ProductReturnRequest request) {
        service.returnProductsToWarehouse(request);
    }

    @Override
    public ResponseEntity<BookedProductsDto> checkQuantityProductInWarehouse(Map<UUID, Long> products) {
        BookedProductsDto response = service.checkQuantityProductInWarehouse(products);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<BookedProductsDto> assemblyProductForOrderFromShoppingCart(AssemblyProductsForOrderRequest request) {
        BookedProductsDto response = service.assemblyProductForOrderFromShoppingCart(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public void addProductQuantityToWarehouse(AddProductToWarehouseRequest request) {
        service.addProductQuantityToWarehouse(request);
    }

    @Override
    public ResponseEntity<AddressDto> getWarehouseAddress() {
        AddressDto response = service.getWarehouseAddress();
        return ResponseEntity.ok(response);
    }
}
