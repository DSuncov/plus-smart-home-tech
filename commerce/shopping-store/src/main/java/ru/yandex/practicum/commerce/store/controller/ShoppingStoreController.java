package ru.yandex.practicum.commerce.store.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.store.facade.ShoppingStoreOrchestrationService;
import ru.yandex.practicum.commerce.utils.dto.store.ProductDto;
import ru.yandex.practicum.commerce.utils.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.utils.enums.ProductCategory;
import ru.yandex.practicum.commerce.utils.feign.store.ShoppingStoreOperations;

import java.util.List;

@RestController
@RequestMapping(name = "/api/v1/shopping-store")
@Validated
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreOperations {

    private final ShoppingStoreOrchestrationService service;

    @Override
    public ResponseEntity<List<ProductDto>> getProductsByCategory(
            @RequestParam @NotNull ProductCategory category,
            @RequestParam(defaultValue = "0") @NotNull Integer page,
            @RequestParam(defaultValue = "20") @NotNull Integer size,
            @RequestParam String sort
    ) {
        List<ProductDto> response = service.getProductsByCategory(category, page, size, sort);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(@PathVariable @NotBlank String productId) {
        ProductDto response = service.getProductById(productId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductDto> createProductInStore(@Valid @NotNull @RequestBody ProductDto productDto) {
        ProductDto response = service.createProductInStore(productDto);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductDto> updateProductInStore(@Valid @NotNull @RequestBody ProductDto productDto) {
        ProductDto response = service.updateProductInStore(productDto);
        return ResponseEntity.ok(response);
    }

    @Override
    public boolean removeProductFromStore(@NotBlank String productId) {
        return service.removeProductFromStore(productId);
    }

    @Override
    public boolean setNewQuantityStatus(@NotNull @Valid @RequestBody SetProductQuantityStateRequest request) {
        return service.setNewQuantityStatus(request);
    }
}
