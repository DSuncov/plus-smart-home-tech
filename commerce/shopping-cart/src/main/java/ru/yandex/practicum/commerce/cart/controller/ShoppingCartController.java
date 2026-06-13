package ru.yandex.practicum.commerce.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.cart.facade.ShoppingCartOrchestrationService;
import ru.yandex.practicum.commerce.cart.service.ShoppingCartService;
import ru.yandex.practicum.commerce.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.exception.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.feign.cart.ShoppingCartOperations;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
@Validated
public class ShoppingCartController implements ShoppingCartOperations {

    private final ShoppingCartService service;
    private final ShoppingCartOrchestrationService orchestrationService;

    @Override
    public ResponseEntity<ShoppingCartDto> getShoppingCartByUsername(String username) throws NotAuthorizedUserException {
        ShoppingCartDto response = service.getShoppingCartByUsername(username);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ShoppingCartDto> addProductToShoppingCart(
            String username,
            Map<UUID, Long> products) throws NotAuthorizedUserException {
        ShoppingCartDto response = orchestrationService.addProductToShoppingCart(username, products);
        return ResponseEntity.ok(response);
    }

    @Override
    public void deactivateShoppingCartByUsername(String username) throws NotAuthorizedUserException {
        service.deactivateShoppingCartByUsername(username);
    }

    @Override
    public ResponseEntity<ShoppingCartDto> removeProductsFromShoppingCart(
            String username,
            String[] ids) throws NotAuthorizedUserException {
        ShoppingCartDto response = service.removeProductsFromShoppingCart(username, ids);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ShoppingCartDto> changeProductQuantity(
            String username,
            ChangeProductQuantityRequest request) throws NotAuthorizedUserException {
        ShoppingCartDto response = orchestrationService.changeProductQuantity(username, request);
        return ResponseEntity.ok(response);
    }
}
