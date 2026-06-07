package ru.yandex.practicum.commerce.utils.feign.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.utils.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.utils.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.utils.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.utils.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.utils.dto.warehouse.ProductDto;

public interface WarehouseOperations {

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    void addProductToWarehouse(@Valid @NotNull @RequestBody ProductDto product);

    @PostMapping("/check")
    ResponseEntity<BookedProductsDto> checkQuantityProductInWarehouse(@Valid @NotNull @RequestBody ShoppingCartDto cart);

    @PostMapping("/add")
    void addProductQuantityToWarehouse(@Valid @NotNull @RequestBody AddProductToWarehouseRequest request);

    @GetMapping("/address")
    ResponseEntity<AddressDto> getWarehouseAddress();
}
