package ru.yandex.practicum.commerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {
        "ru.yandex.practicum.commerce.feign.delivery",
        "ru.yandex.practicum.commerce.feign.payment",
        "ru.yandex.practicum.commerce.feign.warehouse"})
@ConfigurationPropertiesScan
public class OrderApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class, args);
    }
}
