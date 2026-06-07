package ru.yandex.practicum.commerce.cart.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.commerce.cart.model.ShoppingCart;
import ru.yandex.practicum.commerce.cart.service.ShoppingCartDatabaseService;
import ru.yandex.practicum.commerce.cart.service.ShoppingCartExternalServiceClient;
import ru.yandex.practicum.commerce.utils.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.utils.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.utils.exception.NoProductsInShoppingCartException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartOrchestrationService {

    private final ShoppingCartDatabaseService databaseService;
    private final ShoppingCartExternalServiceClient externalServiceClient;
    private final ShoppingCartMapper mapper;

    // Просто выгружаем данные из БД
    public ShoppingCartDto getShoppingCartByUsername(String username) {
        ShoppingCartDto response = databaseService.getShoppingCartByUsername(username);

        if (response.products().isEmpty()) {
            throw new NoProductsInShoppingCartException("Корзина пуста.");
        }

        return response;
    }

    // Чтобы добавить товар
    // 1. Получаем текущую корзину для пользователя
    // Если ее нет, то создаем в методе getShoppingCartByUsername
    // 2. Получаем данные о доступном количестве на складе
    // 3. Если кол-ва досточно, то маппим в сущность
    // 4. Устанавливаем новое количество
    // 5. Сохраняем новую корзину в БД в методе addProductToShoppingCart

    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> products) {
        ShoppingCartDto current = databaseService.getShoppingCartByUsername(username);
        externalServiceClient.isEnoughQuantity(current);
        ShoppingCart cart = mapper.toEntity(current);
        cart.setProducts(products);
        return databaseService.addProductToShoppingCart(cart);
    }

    // Просто меняем статус у сущности сохраняем изменения в БД
    public void deactivateShoppingCartByUsername(String username) {
        databaseService.deactivateShoppingCartByUsername(username);
    }

    // Просто удаляем товары у сущности и сохраняем изменения в БД
    public ShoppingCartDto removeProductsFromShoppingCart(String username, List<UUID> ids) {
        return databaseService.removeProductsFromShoppingCart(username, ids);
    }

    // Чтобы изменить количество товара в корзине
    // 1. Проверяем в БД, что корзина есть
    // Если ее нет, то создаем в методе getShoppingCartByUsername
    // 2. Проверяем, что товар есть в корзине
    // Если нет, то добавляем в корзину

    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCartDto current = databaseService.getShoppingCartByUsername(username);
        boolean productIsExistInCart = databaseService.checkOnExistCartByUsernameAndProductId(current, request);
        externalServiceClient.isEnoughQuantity(current);

        if (productIsExistInCart) {
            current.products().put(request.productId(), request.newQuantity());
        }

        databaseService.saveUpdatedCart(current);

        return current;
    }
}
