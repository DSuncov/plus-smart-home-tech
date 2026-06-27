package ru.yandex.practicum.commerce.feign.payment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface PaymentOperations {

    @PostMapping
    ResponseEntity<PaymentDto> createPayment(@RequestBody @NotNull @Valid OrderDto request);

    @PostMapping("/totalCost")
    ResponseEntity<BigDecimal> calculateTotalCost(@RequestBody @NotNull @Valid OrderDto request);

    @PostMapping("/refund")
    void refundPayment(@RequestBody @NotNull UUID paymentId);

    @PostMapping("/productCost")
    ResponseEntity<BigDecimal> calculateProductCost(@RequestBody @Valid @NotNull Map<@NotNull UUID, Long> products);

    @PostMapping("/failed")
    void failedPayment(@RequestBody @NotNull UUID paymentId);
}
