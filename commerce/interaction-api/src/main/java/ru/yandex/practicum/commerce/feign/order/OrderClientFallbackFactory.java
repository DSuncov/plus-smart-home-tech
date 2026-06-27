package ru.yandex.practicum.commerce.feign.order;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderClientFallbackFactory implements FallbackFactory<OrderClient> {
    @Override
    public OrderClient create(Throwable cause) {
        if (cause instanceof RuntimeException) {
            return new OrderFallback();
        }

        return null;
    }
}
