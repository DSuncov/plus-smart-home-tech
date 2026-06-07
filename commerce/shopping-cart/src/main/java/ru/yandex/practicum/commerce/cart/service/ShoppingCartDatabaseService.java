package ru.yandex.practicum.commerce.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.commerce.cart.model.ShoppingCart;
import ru.yandex.practicum.commerce.cart.repository.ShoppingCartRepository;
import ru.yandex.practicum.commerce.utils.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.utils.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.utils.enums.CartState;
import ru.yandex.practicum.commerce.utils.exception.CartNotFoundException;
import ru.yandex.practicum.commerce.utils.exception.NoProductsInShoppingCartException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartDatabaseService {

    private final ShoppingCartRepository repository;
    private final ShoppingCartMapper mapper;

    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCartByUsername(String username) {
        ShoppingCart cart = repository.findByUsername(username).orElseGet(() -> ShoppingCart.builder()
                .username(username)
                .state(CartState.ACTIVE)
                .products(new HashMap<>())
                .build());
        return mapper.toDto(cart);
    }

    @Transactional
    public ShoppingCartDto addProductToShoppingCart(ShoppingCart cart) {
        ShoppingCart response = repository.save(cart);
        return mapper.toDto(response);
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
    public ShoppingCartDto removeProductsFromShoppingCart(String username, List<UUID> ids) {
        ShoppingCart cart = repository.findByUsername(username).orElseThrow(() -> new CartNotFoundException("Корзина для пользователя не найдена."));

        log.info("Удаляем товары из корзины.");
        Map<UUID, Long> products = cart.getProducts();
        ids.forEach(products.keySet()::remove);
        cart.setProducts(products);

        repository.save(cart);

        log.info("Отправляем обновленную корзину клиенту.");
        return getShoppingCartByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean checkOnExistCartByUsernameAndProductId(ShoppingCartDto cart, ChangeProductQuantityRequest request) {
        log.info("Выполнем проверку на наличие товара в корзине.");
        boolean isExist = cart.products().containsKey(request.productId());

        if (!isExist) {
            throw new NoProductsInShoppingCartException("Товар отсутствует в корзине.");
        } else {
            return true;
        }
    }

    @Transactional
    public void saveUpdatedCart(ShoppingCartDto cart) {
        repository.save(mapper.toEntity(cart));
    }
}
