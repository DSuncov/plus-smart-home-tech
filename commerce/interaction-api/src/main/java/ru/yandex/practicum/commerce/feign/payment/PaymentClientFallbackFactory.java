package ru.yandex.practicum.commerce.feign.payment;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class PaymentClientFallbackFactory implements FallbackFactory<PaymentClient> {
    @Override
    public PaymentClient create(Throwable cause) {
        if (cause instanceof RuntimeException) {
            return new PaymentFallback();
        }

        return null;
    }
}
