package ru.yandex.practicum.commerce.payment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.payment.model.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "paymentId", target = "paymentId")
    @Mapping(source = "totalPayment", target = "totalPayment")
    @Mapping(source = "deliveryTotal", target = "deliveryTotal")
    @Mapping(source = "feeTotal", target = "feeTotal")
    PaymentDto toDto(Payment entity);

    @Mapping(source = "paymentId", target = "paymentId")
    @Mapping(source = "totalPayment", target = "totalPayment")
    @Mapping(source = "deliveryTotal", target = "deliveryTotal")
    @Mapping(source = "feeTotal", target = "feeTotal")
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "productTotal", ignore = true)
    @Mapping(target = "state", ignore = true)
    Payment toEntity(PaymentDto paymentDto);
}
