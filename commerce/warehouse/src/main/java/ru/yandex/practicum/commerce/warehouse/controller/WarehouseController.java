package ru.yandex.practicum.commerce.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.warehouse.ProductDto;
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
    public ResponseEntity<BookedProductsDto> checkQuantityProductInWarehouse(Map<UUID, Long> products) {
        BookedProductsDto response = service.checkQuantityProductInWarehouse(products);
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
