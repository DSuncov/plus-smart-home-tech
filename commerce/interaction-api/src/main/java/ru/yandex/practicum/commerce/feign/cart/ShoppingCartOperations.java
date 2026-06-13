package ru.yandex.practicum.commerce.feign.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.exception.NotAuthorizedUserException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartOperations {

    @GetMapping
    ResponseEntity<ShoppingCartDto> getShoppingCartByUsername(@RequestParam @NotBlank String username) throws NotAuthorizedUserException;

    @PutMapping
    ResponseEntity<ShoppingCartDto> addProductToShoppingCart(@RequestParam String username, @RequestBody @Valid @NotNull Map<@NotNull UUID, Long> products)
            throws NotAuthorizedUserException;

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    void deactivateShoppingCartByUsername(@RequestParam @NotBlank String username) throws NotAuthorizedUserException;

    @PostMapping("/remove")
    ResponseEntity<ShoppingCartDto> removeProductsFromShoppingCart(@RequestParam @NotBlank String username, @RequestBody @NotNull String[] ids)
            throws NotAuthorizedUserException;

    @PostMapping("/change-quantity")
    ResponseEntity<ShoppingCartDto> changeProductQuantity(@RequestParam @NotBlank String username, @RequestBody @Valid @NotNull ChangeProductQuantityRequest request)
            throws NotAuthorizedUserException;
}
