package ru.yandex.practicum.commerce.utils.feign.store;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.commerce.utils.dto.store.ProductDto;
import ru.yandex.practicum.commerce.utils.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.utils.enums.ProductCategory;

import java.util.List;

public class ShoppingStoreFallback implements ShoppingStoreClient {

    @Override
    public ResponseEntity<List<ProductDto>> getProductsByCategory(ProductCategory category, Integer page, Integer size, String sort) {
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
    public boolean removeProductFromStore(String productId) {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public boolean setNewQuantityStatus(SetProductQuantityStateRequest request) {
        throw new RuntimeException("Сервис недоступен.");
    }
}
