package ru.yandex.practicum.commerce.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.store.model.Product;
import ru.yandex.practicum.commerce.dto.store.ProductDto;

@Mapper(componentModel = "spring")
public interface ShoppingStoreMapper {

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.description", target = "description")
    @Mapping(source = "product.imageSrc", target = "imageSrc")
    @Mapping(source = "product.quantityState", target = "quantityState")
    @Mapping(source = "product.productState", target = "productState")
    @Mapping(source = "product.productCategory", target = "productCategory")
    @Mapping(source = "product.price", target = "price")
    ProductDto toDto(Product product);

    @Mapping(target = "productId", ignore = true)
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.description", target = "description")
    @Mapping(source = "product.imageSrc", target = "imageSrc")
    @Mapping(source = "product.quantityState", target = "quantityState")
    @Mapping(source = "product.productState", target = "productState")
    @Mapping(source = "product.productCategory", target = "productCategory")
    @Mapping(source = "product.price", target = "price")
    Product toEntity(ProductDto product);
}
