package ru.yandex.practicum.commerce.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.dto.warehouse.DimensionDto;
import ru.yandex.practicum.commerce.dto.warehouse.ProductDto;
import ru.yandex.practicum.commerce.warehouse.model.Dimension;
import ru.yandex.practicum.commerce.warehouse.model.Product;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    @Mapping(source = "dto.productId", target = "productId")
    @Mapping(source = "dto.fragile", target = "fragile")
    @Mapping(source = "dto.dimension", target = "dimension")
    @Mapping(source = "dto.weight", target = "weight")
    @Mapping(target = "quantity", ignore = true)
    Product toEntity(ProductDto dto);

    Dimension toEntity(DimensionDto dto);
}
