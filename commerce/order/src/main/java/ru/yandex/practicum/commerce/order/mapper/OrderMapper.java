package ru.yandex.practicum.commerce.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.order.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "shoppingCartId", target = "shoppingCartId")
    @Mapping(source = "products", target = "products")
    @Mapping(source = "paymentId", target = "paymentId")
    @Mapping(source = "deliveryId", target = "deliveryId")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "deliveryWeight", target = "deliveryWeight")
    @Mapping(source = "deliveryVolume", target = "deliveryVolume")
    @Mapping(source = "fragile", target = "fragile")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "deliveryPrice", target = "deliveryPrice")
    @Mapping(source = "productsPrice", target = "productsPrice")
    OrderDto toDto(Order entity);

    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "shoppingCartId", target = "shoppingCartId")
    @Mapping(source = "products", target = "products")
    @Mapping(source = "paymentId", target = "paymentId")
    @Mapping(source = "deliveryId", target = "deliveryId")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "deliveryWeight", target = "deliveryWeight")
    @Mapping(source = "deliveryVolume", target = "deliveryVolume")
    @Mapping(source = "fragile", target = "fragile")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "deliveryPrice", target = "deliveryPrice")
    @Mapping(source = "productsPrice", target = "productsPrice")
    @Mapping(target = "username", ignore = true)
    Order toEntity(OrderDto orderDto);
}
