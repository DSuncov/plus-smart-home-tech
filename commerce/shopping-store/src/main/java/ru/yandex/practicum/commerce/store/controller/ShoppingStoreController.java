package ru.yandex.practicum.commerce.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.dto.store.PageProductDto;
import ru.yandex.practicum.commerce.dto.store.ProductDto;
import ru.yandex.practicum.commerce.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.enums.ProductCategory;
import ru.yandex.practicum.commerce.feign.store.ShoppingStoreOperations;
import ru.yandex.practicum.commerce.store.service.ShoppingStoreService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@Validated
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreOperations {

    private final ShoppingStoreService service;

    @Override
    public ResponseEntity<PageProductDto> getProductsByCategory(
            ProductCategory category,
            Integer page,
            Integer size,
            Sort sort
    ) {
        PageProductDto response = service.getProductsByCategory(category, page, size, sort);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(String productId) {
        ProductDto response = service.getProductById(productId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductDto> createProductInStore(ProductDto productDto) {
        ProductDto response = service.createProductInStore(productDto);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductDto> updateProductInStore(ProductDto productDto) {
        ProductDto response = service.updateProductInStore(productDto);
        return ResponseEntity.ok(response);
    }

    @Override
    public boolean removeProductFromStore(UUID productId) {
        return service.removeProductFromStore(productId);
    }

    @Override
    public boolean setNewQuantityStatus(UUID productId, String quantityState) {
        SetProductQuantityStateRequest request = new SetProductQuantityStateRequest(productId, quantityState);
        return service.setNewQuantityStatus(request);
    }
}
