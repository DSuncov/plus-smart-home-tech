package ru.yandex.practicum.commerce.utils.feign.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.utils.dto.store.ProductDto;
import ru.yandex.practicum.commerce.utils.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.utils.enums.ProductCategory;

import java.util.List;

public interface ShoppingStoreOperations {

    @GetMapping
    ResponseEntity<List<ProductDto>> getProductsByCategory(
            @RequestParam @NotNull ProductCategory category,
            @RequestParam(defaultValue = "0") @NotNull Integer page,
            @RequestParam(defaultValue = "20") @NotNull Integer size,
            @RequestParam @NotEmpty String sort
    );

    @GetMapping("/{productId}")
    ResponseEntity<ProductDto> getProductById(@PathVariable @NotBlank String productId);

    @PutMapping
    ResponseEntity<ProductDto> createProductInStore(@Valid @NotNull @RequestBody ProductDto productDto);

    @PostMapping
    ResponseEntity<ProductDto> updateProductInStore(@Valid @NotNull @RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    boolean removeProductFromStore(@NotBlank String productId);

    @PostMapping("/quantityState")
    boolean setNewQuantityStatus(@NotNull @Valid @RequestBody SetProductQuantityStateRequest request);
}
