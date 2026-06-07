package ru.yandex.practicum.commerce.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.store.PageProductDto;
import ru.yandex.practicum.commerce.dto.store.ProductDto;
import ru.yandex.practicum.commerce.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.enums.ProductCategory;
import ru.yandex.practicum.commerce.enums.ProductState;
import ru.yandex.practicum.commerce.enums.QuantityState;
import ru.yandex.practicum.commerce.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.store.mapper.ShoppingStoreMapper;
import ru.yandex.practicum.commerce.store.model.Product;
import ru.yandex.practicum.commerce.store.repository.ShoppingStoreRepository;
import ru.yandex.practicum.commerce.utils.PageableObject;
import ru.yandex.practicum.commerce.utils.SortObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingStoreService {

    private final ShoppingStoreRepository repository;
    private final ShoppingStoreMapper mapper;
    private final PageableHandler pageableHandler;

    @Transactional(readOnly = true)
    public PageProductDto getProductsByCategory(ProductCategory category, Integer page, Integer size, Sort sort) {
        if (sort == null || sort.isEmpty()) {
            sort = Sort.unsorted();
        }

        PageableObject pageableObject = pageableHandler.createPageableObject(page, size, sort);

        Pageable pageable = createPageable(pageableObject, sort);

        Page<Product> products = repository.findByCategory(category, pageable);

        return convertToPageProductDto(products);
    }

    private PageProductDto convertToPageProductDto(Page<Product> products) {
        return new PageProductDto(
            products.getTotalElements(),
            products.getTotalPages(),
            products.isFirst(),
            products.isLast(),
            products.getSize(),
            convertToProductDto(products.getContent()),
            products.getNumber(),
            convertToSortObject(products.getSort()),
            products.getNumberOfElements(),
            convertToPageableObject(products.getPageable()),
            products.isEmpty()
        );
    }

    private List<ProductDto> convertToProductDto(List<Product> products) {
        return products.stream()
                .map(mapper::toDto)
                .toList();
    }

    private List<SortObject> convertToSortObject(Sort sort) {
        List<SortObject> sortObjects = new ArrayList<>();

        for (Sort.Order order : sort) {
            SortObject sortObject = new SortObject(
                    order.getDirection().name(),
                    order.getNullHandling().name(),
                    order.isDescending(),
                    order.getProperty(),
                    order.isIgnoreCase()
            );

            sortObjects.add(sortObject);
        }

        return sortObjects;
    }

    private PageableObject convertToPageableObject(Pageable pageable) {
        Sort sort = pageable.getSort();

        return new PageableObject(
                pageable.getOffset(),
                convertToSortObject(sort),
                pageable.isUnpaged(),
                pageable.isPaged(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    private Pageable createPageable(PageableObject pageableObject, Sort sort) {
        return PageRequest.of(pageableObject.pageNumber(), pageableObject.pageSize(), sort);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(String productId) {
        Product product = repository.findById(UUID.fromString(productId)).orElseThrow(() -> new ProductNotFoundException("Товара с таким id не существует."));
        return mapper.toDto(product);
    }

    @Transactional
    public ProductDto createProductInStore(ProductDto productDto) {
        Product product = repository.save(mapper.toEntity(productDto));
        return mapper.toDto(product);
    }

    @Transactional
    public ProductDto updateProductInStore(ProductDto productDto) {
        repository.findById(productDto.productId()).orElseThrow(() -> new ProductNotFoundException("Товара с таким id не существует."));
        Product updatedProduct = mapper.toEntity(productDto);
        repository.save(updatedProduct);
        return mapper.toDto(updatedProduct);
    }

    @Transactional
    public boolean removeProductFromStore(UUID productId) {
        log.info("Проверяем наличие товара с id = {}", productId);
        Product product = repository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Товара с таким id не существует."));
        log.info("Товар найден. Выполняем удаление из ассортимента магазина.");
        product.setProductState(ProductState.DEACTIVATE);
        Product updatedProduct = repository.save(product);

        return updatedProduct.getProductState().equals(ProductState.DEACTIVATE);
    }

    @Transactional
    public boolean setNewQuantityStatus(SetProductQuantityStateRequest request) {
        Product product = repository.findById(UUID.fromString(String.valueOf(request.productId()))).orElseThrow(() -> new ProductNotFoundException("Товара с таким id не существует."));
        product.setQuantityState(QuantityState.valueOf(request.quantityState()));
        Product updatedProduct = repository.save(product);
        return product.getQuantityState().equals(updatedProduct.getQuantityState());
    }
}
