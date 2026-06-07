package ru.yandex.practicum.commerce.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.commerce.cart.model.ShoppingCart;
import ru.yandex.practicum.commerce.cart.repository.ShoppingCartRepository;
import ru.yandex.practicum.commerce.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.enums.CartState;
import ru.yandex.practicum.commerce.exception.CartNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartService {

    private final ShoppingCartRepository repository;
    private final ShoppingCartMapper mapper;

    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCartByUsername(String username) {
        ShoppingCart cart = repository.findByUsername(username).orElseThrow(() -> new CartNotFoundException("Корзина для пользователя не найдена."));
        return mapper.toDto(cart);
    }

    @Transactional
    public ShoppingCart getOrCreateCartByUsername(String username) {
        ShoppingCart currentCart = repository.findByUsername(username).orElseGet(() -> ShoppingCart.builder()
                .username(username)
                .state(CartState.ACTIVE)
                .products(new HashMap<>())
                .build());

        return currentCart;
    }

    @Transactional
    public ShoppingCartDto addProductToShoppingCart(ShoppingCart cart, Map<UUID, Long> products) {
        Map<UUID, Long> currentProducts = cart.getProducts();

        for (UUID key : products.keySet()) {
            currentProducts.put(key, products.get(key));
        }

        cart.setProducts(currentProducts);

        ShoppingCart newCart = repository.save(cart);

        return mapper.toDto(newCart);
    }

    @Transactional
    public void deactivateShoppingCartByUsername(String username) {
        ShoppingCart cart = repository.findByUsername(username).orElseThrow(() -> new CartNotFoundException("Корзина для пользователя не найдена."));

        log.info("Выполняем деактивацию корзины товаров.");
        cart.setState(CartState.DEACTIVATE);

        repository.save(cart);
        log.info("Корзина успешно деактивирована.");
    }

    @Transactional
    public ShoppingCartDto removeProductsFromShoppingCart(String username, String[] ids) {
        ShoppingCart cart = repository.findByUsername(username).orElseThrow(() -> new CartNotFoundException("Корзина для пользователя не найдена."));

        log.info("Удаляем товары из корзины.");
        Map<UUID, Long> products = cart.getProducts();

        for (String id : ids) {
            products.keySet().removeIf(key -> key.equals(UUID.fromString(id)));
        }

        cart.setProducts(products);

        repository.save(cart);

        log.info("Отправляем обновленную корзину клиенту.");
        return getShoppingCartByUsername(username);
    }

    @Transactional
    public ShoppingCart getCart(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new CartNotFoundException("Корзина для пользователя не найдена."));
    }

    @Transactional
    public ShoppingCartDto changeProductQuantity(ShoppingCart cart, ChangeProductQuantityRequest request) {
        cart.getProducts().put(request.productId(), request.newQuantity());
        return mapper.toDto(repository.save(cart));
    }
}
