package ru.yandex.practicum.commerce.feign.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.delivery.ShippedRequest;
import ru.yandex.practicum.commerce.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseOperations {

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    void addProductToWarehouse(@Valid @NotNull @RequestBody ProductDto product);

    @PostMapping("/shipped")
    @ResponseStatus(HttpStatus.OK)
    void shipped(@RequestBody @NotNull @Valid ShippedRequest request);

    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    void returnToWarehouse(@RequestBody @NotNull @Valid ProductReturnRequest request);

    @PostMapping("/check")
    ResponseEntity<BookedProductsDto> checkQuantityProductInWarehouse(@Valid @NotNull @RequestBody Map<UUID, Long> products);

    @PostMapping("/assembly")
    ResponseEntity<BookedProductsDto> assemblyProductForOrderFromShoppingCart(@RequestBody @NotNull @Valid AssemblyProductsForOrderRequest request);

    @PostMapping("/add")
    void addProductQuantityToWarehouse(@Valid @NotNull @RequestBody AddProductToWarehouseRequest request);

    @GetMapping("/address")
    ResponseEntity<AddressDto> getWarehouseAddress();
}
