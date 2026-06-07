package ru.yandex.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.utils.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.utils.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.utils.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.utils.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.utils.dto.warehouse.ProductDto;
import ru.yandex.practicum.commerce.utils.feign.warehouse.WarehouseOperations;
import ru.yandex.practicum.commerce.warehouse.facade.WarehouseOrchestrationService;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/warehouse")
public class WarehouseController implements WarehouseOperations {

    private final WarehouseOrchestrationService service;

    @Override
    public void addProductToWarehouse(@Valid @NotNull @RequestBody ProductDto product) {
        service.addProductToWarehouse(product);
    }

    @Override
    public ResponseEntity<BookedProductsDto> checkQuantityProductInWarehouse(@Valid @NotNull @RequestBody ShoppingCartDto cart) {
        BookedProductsDto response = service.checkQuantityProductInWarehouse(cart);
        return ResponseEntity.ok(response);
    }

    @Override
    public void addProductQuantityToWarehouse(@Valid @NotNull @RequestBody AddProductToWarehouseRequest request) {
        service.addProductQuantityToWarehouse(request);
    }

    @Override
    public ResponseEntity<AddressDto> getWarehouseAddress() {
        AddressDto response = service.getWarehouseAddress();
        return ResponseEntity.ok(response);
    }
}
