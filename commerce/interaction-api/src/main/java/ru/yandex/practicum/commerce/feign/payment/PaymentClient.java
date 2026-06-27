package ru.yandex.practicum.commerce.feign.payment;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "payment", path = "/api/v1/payment",
        fallback = PaymentFallback.class, fallbackFactory = PaymentClientFallbackFactory.class)
public interface PaymentClient extends PaymentOperations {
}
