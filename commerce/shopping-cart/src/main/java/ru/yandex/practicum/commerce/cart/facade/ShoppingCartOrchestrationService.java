package ru.yandex.practicum.commerce.cart.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.cart.model.ShoppingCart;
import ru.yandex.practicum.commerce.cart.service.ShoppingCartExternalService;
import ru.yandex.practicum.commerce.cart.service.ShoppingCartService;
import ru.yandex.practicum.commerce.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartOrchestrationService {

    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartExternalService shoppingCartExternalService;

    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> products) {
        ShoppingCart cart = shoppingCartService.getOrCreateCartByUsername(username);
        shoppingCartExternalService.checkQuantity(products);
        return shoppingCartService.addProductToShoppingCart(cart, products);
    }

    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCart cart = shoppingCartService.getCart(username);
        Map<UUID, Long> products = new HashMap<>();
        products.put(request.productId(), request.newQuantity());
        shoppingCartExternalService.checkQuantity(products);
        return shoppingCartService.changeProductQuantity(cart, request);
    }
}
