package ru.yandex.practicum.commerce.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {
        "ru.yandex.practicum.commerce.feign.order",
        "ru.yandex.practicum.commerce.feign.store"})
@ConfigurationPropertiesScan
public class PaymentApp {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApp.class, args);
    }
}
