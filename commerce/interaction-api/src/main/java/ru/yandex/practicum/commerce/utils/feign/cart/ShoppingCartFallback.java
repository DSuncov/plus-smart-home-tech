package ru.yandex.practicum.commerce.utils.feign.cart;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.commerce.utils.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.utils.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.utils.exception.NotAuthorizedUserException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShoppingCartFallback implements ShoppingCartClient{

    @Override
    public ResponseEntity<ShoppingCartDto> getShoppingCartByUsername(String username) throws NotAuthorizedUserException {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<ShoppingCartDto> addProductToShoppingCart(String username, Map<UUID, Long> products) throws NotAuthorizedUserException {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public void deactivateShoppingCartByUsername(String username) throws NotAuthorizedUserException {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<ShoppingCartDto> removeProductsFromShoppingCart(String username, List<UUID> ids) throws NotAuthorizedUserException {
        throw new RuntimeException("Сервис недоступен.");
    }

    @Override
    public ResponseEntity<ShoppingCartDto> changeProductQuantity(String username, ChangeProductQuantityRequest request) throws NotAuthorizedUserException {
        throw new RuntimeException("Сервис недоступен.");
    }
}
