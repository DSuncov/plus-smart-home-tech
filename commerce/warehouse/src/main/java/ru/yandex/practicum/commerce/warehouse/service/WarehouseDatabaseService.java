package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.utils.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.utils.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.utils.dto.warehouse.ProductDto;
import ru.yandex.practicum.commerce.utils.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.utils.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.utils.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.commerce.warehouse.model.Dimension;
import ru.yandex.practicum.commerce.warehouse.model.Product;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseDatabaseService {

    private final WarehouseRepository repository;
    private final WarehouseMapper mapper;

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
        Map<UUID, Long> current = repository.findProductDetails(products.keySet());

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
                    return (Double) (dimension.getHeight() * dimension.getWidth() * dimension.getDepth());
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
}
