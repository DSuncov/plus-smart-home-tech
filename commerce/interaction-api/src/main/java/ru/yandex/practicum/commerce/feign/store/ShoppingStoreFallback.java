package ru.yandex.practicum.commerce.feign.store;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.commerce.dto.store.PageProductDto;
import ru.yandex.practicum.commerce.dto.store.ProductDto;
import ru.yandex.practicum.commerce.enums.ProductCategory;

import java.util.UUID;

public class ShoppingStoreFallback implements ShoppingStoreClient {

    @Override
    public ResponseEntity<PageProductDto> getProductsByCategory(ProductCategory category, Integer page, Integer size, Sort sort) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(String productId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<ProductDto> createProductInStore(ProductDto productDto) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<ProductDto> updateProductInStore(ProductDto productDto) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public boolean removeProductFromStore(UUID productId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public boolean setNewQuantityStatus(UUID productId, String quantityState) {
        throw new RuntimeException("Сервис недоступен.");
    }
}
