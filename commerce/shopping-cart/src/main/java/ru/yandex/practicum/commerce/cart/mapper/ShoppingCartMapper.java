package ru.yandex.practicum.commerce.cart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.cart.model.ShoppingCart;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {

    @Mapping(source = "cart.shoppingCartId", target = "shoppingCartId")
    @Mapping(source = "cart.products", target = "products")
    ShoppingCartDto toDto(ShoppingCart cart);

    @Mapping(target = "shoppingCartId", ignore = true)
    @Mapping(target = "state", ignore = true)
    ShoppingCart toEntity(ShoppingCartDto dto);
}
