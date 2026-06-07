package ru.yandex.practicum.commerce.cart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.cart.facade.ShoppingCartOrchestrationService;
import ru.yandex.practicum.commerce.utils.feign.cart.ShoppingCartOperations;
import ru.yandex.practicum.commerce.utils.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.utils.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.utils.exception.NotAuthorizedUserException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(name = "/api/v1/shopping-cart")
@RequiredArgsConstructor
@Validated
public class ShoppingCartController implements ShoppingCartOperations {

    private final ShoppingCartOrchestrationService service;

    @Override
    public ResponseEntity<ShoppingCartDto> getShoppingCartByUsername(@RequestParam @NotBlank String username) throws NotAuthorizedUserException {
        ShoppingCartDto response = service.getShoppingCartByUsername(username);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ShoppingCartDto> addProductToShoppingCart(
            @RequestParam String username,
            @RequestBody @Valid @NotNull Map<@NotNull UUID, @NotNull @Positive Long> products) throws NotAuthorizedUserException {
        ShoppingCartDto response = service.addProductToShoppingCart(username, products);
        return ResponseEntity.ok(response);
    }

    @Override
    public void deactivateShoppingCartByUsername(@RequestParam @NotBlank String username) throws NotAuthorizedUserException {
        service.deactivateShoppingCartByUsername(username);
    }

    @Override
    public ResponseEntity<ShoppingCartDto> removeProductsFromShoppingCart(
            @RequestParam @NotBlank String username,
            @NotEmpty List<UUID> ids) throws NotAuthorizedUserException {
        ShoppingCartDto response = service.removeProductsFromShoppingCart(username, ids);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ShoppingCartDto> changeProductQuantity(
            @RequestParam @NotBlank String username,
            @RequestBody @Valid @NotNull ChangeProductQuantityRequest request) throws NotAuthorizedUserException {
        ShoppingCartDto response = service.changeProductQuantity(username, request);
        return ResponseEntity.ok(response);
    }
}
