package ru.yandex.practicum.commerce.delivery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.delivery.model.Delivery;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(source = "deliveryId", target = "deliveryId")
    @Mapping(source = "addressFrom", target = "addressFrom")
    @Mapping(source = "addressTo", target = "addressTo")
    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "state", target = "state")
    DeliveryDto toDto(Delivery delivery);

    @Mapping(source = "deliveryId", target = "deliveryId")
    @Mapping(source = "addressFrom", target = "addressFrom")
    @Mapping(source = "addressTo", target = "addressTo")
    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "state", target = "state")
    Delivery toEntity(DeliveryDto deliveryDto);
}
