package ru.yandex.practicum.commerce.feign.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.store.PageProductDto;
import ru.yandex.practicum.commerce.dto.store.ProductDto;
import ru.yandex.practicum.commerce.enums.ProductCategory;

import java.util.UUID;

public interface ShoppingStoreOperations {

    @GetMapping
    ResponseEntity<PageProductDto> getProductsByCategory(
            @RequestParam @NotNull ProductCategory category,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            Sort sort
    );

    @GetMapping("/{productId}")
    ResponseEntity<ProductDto> getProductById(@PathVariable @NotBlank String productId);

    @PutMapping
    ResponseEntity<ProductDto> createProductInStore(@Valid @NotNull @RequestBody ProductDto productDto);

    @PostMapping
    ResponseEntity<ProductDto> updateProductInStore(@Valid @NotNull @RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    boolean removeProductFromStore(@NotNull @RequestBody UUID productId);

    @PostMapping("/quantityState")
    boolean setNewQuantityStatus(
            @RequestParam @NotNull UUID productId,
            @RequestParam @NotBlank String quantityState);
}
