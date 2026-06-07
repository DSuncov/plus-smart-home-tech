package ru.yandex.practicum.commerce.store.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.store.service.ShoppingStoreDatabaseService;
import ru.yandex.practicum.commerce.store.service.ShoppingStoreExternalServiceClient;
import ru.yandex.practicum.commerce.utils.dto.store.ProductDto;
import ru.yandex.practicum.commerce.utils.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.utils.enums.ProductCategory;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingStoreOrchestrationService {

    private final ShoppingStoreDatabaseService databaseService;
    private final ShoppingStoreExternalServiceClient externalServiceClient;

    public List<ProductDto> getProductsByCategory(ProductCategory category, Integer page, Integer size, String sort) {
        log.info("Отправляем запрос на получие списка товаров.");
        List<ProductDto> response = databaseService.getProductsByCategory(category, page, size, sort);
        log.info("Список товаров успешно получен.");
        return response;
    }

    public ProductDto getProductById(String productId) {
        log.info("Отправляем запрос на получие информации о товаре.");
        ProductDto response = databaseService.getProductById(productId);
        log.info("Информация о товаре успешно получена.");
        return response;
    }

    public ProductDto createProductInStore(ProductDto productDto) {
        log.info("Отправляем запрос на создание нового товара в ассортименте.");
        ProductDto response = databaseService.createProductInStore(productDto);
        log.info("Товар добавлен в ассортимент магазина.");
        return response;
    }

    public ProductDto updateProductInStore(ProductDto productDto) {
        log.info("Отправляем запрос на изменение информации о товаре в ассортименте.");
        ProductDto response = databaseService.updateProductInStore(productDto);
        log.info("Информация о товаре успешно обновлена.");
        return response;
    }

    public boolean removeProductFromStore(String productId) {
        log.info("Выполняем удаление товара из ассортимента магазина.");
        return databaseService.removeProductFromStore(productId);
    }

    public boolean setNewQuantityStatus(SetProductQuantityStateRequest request) {
        log.info("Отправляем запром на обновление статуса товара в магазине.");
        return databaseService.setNewQuantityStatus(request);
    }
}
