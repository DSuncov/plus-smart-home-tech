package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.warehouse.ProductDto;
import ru.yandex.practicum.commerce.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.commerce.warehouse.model.Dimension;
import ru.yandex.practicum.commerce.warehouse.model.Product;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository repository;
    private final WarehouseMapper mapper;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    @Transactional
    public void addProductToWarehouse(ProductDto product) {
        if (repository.existsById(product.productId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Товар уже есть на складе.");
        }

        Product entity = mapper.toEntity(product);
        repository.save(entity);
    }

    @Transactional(readOnly = true)
    public BookedProductsDto checkQuantityProductInWarehouse(Map<UUID, Long> products) {
        List<Object[]> results = repository.findProductDetails(products.keySet());
        Map<UUID, Long> current = new HashMap<>();

        for (Object[] row : results) {
            UUID productId = (UUID) row[0];
            Long quantity = (Long) row[1];

            current.put(productId, quantity);
        }

        boolean equal = Objects.equals(products.keySet(), current.keySet());

        if (!equal) {
            throw new NoSpecifiedProductInWarehouseException("Товары отсутствуют на складе.");
        }

        boolean isNotEnough = products.entrySet().stream()
                .anyMatch(entry -> {
                    Long cartValue = entry.getValue();
                    Long warehouseValue = current.get(entry.getKey());

                    return cartValue > warehouseValue;
                });

        if (isNotEnough) {
            throw new ProductInShoppingCartLowQuantityInWarehouse("Количество товаров на складе недостаточно для корзины.");
        }

        List<Product> bookedProducts = repository.findProducts(products.keySet());

        boolean fragile = bookedProducts.stream().anyMatch(Product::getFragile);

        Double deliveryWeight = bookedProducts.stream()
                .mapToDouble(Product::getWeight)
                .sum();

        Double deliveryVolume = bookedProducts.stream()
                .mapToDouble(p -> {
                    Dimension dimension = p.getDimension();
                    return dimension.getHeight() * dimension.getWidth() * dimension.getDepth();
                })
                .sum();

        return new BookedProductsDto(deliveryWeight, deliveryVolume, fragile);
    }

    @Transactional
    public void addProductQuantityToWarehouse(AddProductToWarehouseRequest request) {
        Product product = repository.findById(request.productId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Товар с таким id отсутствует на складе."));
        product.setQuantity(product.getQuantity() + request.quantity());
        repository.save(product);
    }

    public AddressDto getWarehouseAddress() {
        log.info("Отправляем запрос на получение адреса склада.");
        AddressDto addressDto = new AddressDto(
                CURRENT_ADDRESS,
                CURRENT_ADDRESS,
                CURRENT_ADDRESS,
                CURRENT_ADDRESS,
                CURRENT_ADDRESS);

        log.info("Информация об адресе склада получена.");
        return addressDto;
    }
}
