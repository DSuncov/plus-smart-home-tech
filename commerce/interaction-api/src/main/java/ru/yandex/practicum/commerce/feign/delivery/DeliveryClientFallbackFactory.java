package ru.yandex.practicum.commerce.feign.delivery;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DeliveryClientFallbackFactory implements FallbackFactory<DeliveryClient> {
    @Override
    public DeliveryClient create(Throwable cause) {
        if (cause instanceof RuntimeException) {
            return new DeliveryFallback();
        }

        return null;
    }
}
