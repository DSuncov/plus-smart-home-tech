package ru.yandex.practicum.commerce.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.store.mapper.ShoppingStoreMapper;
import ru.yandex.practicum.commerce.store.model.Product;
import ru.yandex.practicum.commerce.store.repository.ShoppingStoreRepository;
import ru.yandex.practicum.commerce.utils.dto.store.ProductDto;
import ru.yandex.practicum.commerce.utils.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.utils.enums.ProductCategory;
import ru.yandex.practicum.commerce.utils.enums.QuantityState;
import ru.yandex.practicum.commerce.utils.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.utils.exception.SpecifiedProductAlreadyInStoreException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingStoreDatabaseService {

    private final ShoppingStoreRepository repository;
    private final ShoppingStoreMapper mapper;

    public List<ProductDto> getProductsByCategory(ProductCategory category, Integer page, Integer size, String sort) {
        Sort.Direction direction = sort.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortType = Sort.by(direction, sort);
        Pageable pageable = PageRequest.of(page, size, sortType);

        List<Product> products = repository.findByCategory(category, pageable).getContent();

        return products.stream()
                .map(mapper::toDto)
                .toList();
    }

    public ProductDto getProductById(String productId) {
        Product product = repository.findById(UUID.fromString(productId)).orElseThrow(() -> new ProductNotFoundException("Товара с таким id не существует."));
        return mapper.toDto(product);
    }

    public ProductDto createProductInStore(ProductDto productDto) {
        Optional<Product> optProduct = repository.findById(productDto.productId());

        if (optProduct.isPresent()) {
            throw new SpecifiedProductAlreadyInStoreException("Товар с таким id уже добавлен в ассортимент магазина.");
        }

        Product product = repository.save(mapper.toEntity(productDto));
        return mapper.toDto(product);
    }

    public ProductDto updateProductInStore(ProductDto productDto) {
        repository.findById(productDto.productId()).orElseThrow(() -> new ProductNotFoundException("Товара с таким id не существует."));
        Product updatedProduct = mapper.toEntity(productDto);
        repository.save(updatedProduct);
        return mapper.toDto(updatedProduct);
    }

    public boolean removeProductFromStore(String productId) {
        repository.findById(UUID.fromString(productId)).orElseThrow(() -> new ProductNotFoundException("Товара с таким id не существует."));
        return repository.deleteByProductId(UUID.fromString(productId));
    }

    public boolean setNewQuantityStatus(SetProductQuantityStateRequest request) {
        Product product = repository.findById(UUID.fromString(String.valueOf(request.productId()))).orElseThrow(() -> new ProductNotFoundException("Товара с таким id не существует."));
        product.setQuantityState(QuantityState.valueOf(request.quantityState()));
        Product updatedProduct = repository.save(product);
        return product.getQuantityState().equals(updatedProduct.getQuantityState());
    }
}
