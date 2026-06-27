package ru.yandex.practicum.commerce.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.enums.PaymentState;
import ru.yandex.practicum.commerce.exception.ConflictException;
import ru.yandex.practicum.commerce.exception.NoPaymentFoundException;
import ru.yandex.practicum.commerce.feign.order.OrderClient;
import ru.yandex.practicum.commerce.feign.store.ShoppingStoreClient;
import ru.yandex.practicum.commerce.payment.mapper.PaymentMapper;
import ru.yandex.practicum.commerce.payment.model.Payment;
import ru.yandex.practicum.commerce.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class PaymentService {

    private static final BigDecimal FEE_RATE = BigDecimal.valueOf(0.1);

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final OrderClient orderClient;
    private final ShoppingStoreClient shoppingStoreClient;

    @Transactional
    public PaymentDto createPayment(OrderDto request) {
        if (repository.existsByOrderId(request.orderId())) {
            throw new NoPaymentFoundException("Платеж с таким идентификатором отсутствует.");
        }

        BigDecimal productsCost = calculateProductsCost(request.products());
        BigDecimal deliveryCost = request.deliveryPrice();
        BigDecimal feeTotal = calculateFee(productsCost);
        BigDecimal totalCost = productsCost.add(feeTotal).add(deliveryCost);

        log.info("Создаем платеж.");
        Payment payment = Payment.builder()
                .orderId(request.orderId())
                .productTotal(productsCost)
                .totalPayment(totalCost)
                .deliveryTotal(deliveryCost)
                .feeTotal(feeTotal)
                .state(PaymentState.PENDING)
                .build();

        log.info("Сохраняем платеж в БД.");
        Payment createdPayment = repository.save(payment);
        return mapper.toDto(createdPayment);
    }

    @Transactional
    public BigDecimal calculateTotalCost(OrderDto request) {
        log.info("Выполняем расчет стоимости доставки.");
        BigDecimal productsPrice = request.productsPrice();

        BigDecimal totalPrice = productsPrice
                .add(productsPrice.multiply(calculateFee(productsPrice)))
                .add(request.deliveryPrice());
        log.info("Отправляем результат клиенту.");
        return totalPrice;
    }

    @Transactional
    public BigDecimal calculateProductsCost(Map<UUID, Long> products) {
        BigDecimal productsPrice = BigDecimal.valueOf(0);

        Map<UUID, BigDecimal> productsCostFromStore = shoppingStoreClient.getProductsPrice(products.keySet());

        for (Map.Entry<UUID, Long> entry : products.entrySet()) {
            BigDecimal productPrice = BigDecimal.valueOf(entry.getValue()).multiply(productsCostFromStore.get(entry.getKey()));
            productsPrice = productsPrice.add(productPrice);
        }

        log.info("Расчет выполнен. Отправляем данные.");

        return productsPrice;
    }

    @Transactional
    public void paymentSuccess(UUID paymentId) {
        log.info("Определяем существует ли платеж.");
        Payment payment = repository.findById(paymentId).orElseThrow(() -> new NoPaymentFoundException("Оплата не найдена."));

        if (payment.getState() == PaymentState.SUCCESS) {
            throw new ConflictException("Оплата заказа уже подтверждена.");
        }

        log.info("Обновляем статусы платежа и заказа.");
        payment.setState(PaymentState.SUCCESS);
        repository.save(payment);

        orderClient.successPayment(payment.getOrderId());
    }

    @Transactional
    public void paymentFailed(UUID paymentId) {
        log.info("Определяем существует ли платеж.");
        Payment payment = repository.findById(paymentId).orElseThrow(() -> new NoPaymentFoundException("Оплата не найдена."));

        if (payment.getState() == PaymentState.SUCCESS) {
            throw new ConflictException("Нельзя отклонить успешный платеж.");
        }

        if (payment.getState() == PaymentState.FAILED) {
            throw new ConflictException("Оплата заказа уже отклонена.");
        }

        log.info("Обновляем статусы платежа и заказа.");
        payment.setState(PaymentState.FAILED);
        repository.save(payment);

        orderClient.failedPayment(payment.getOrderId());
    }

    private BigDecimal calculateFee(BigDecimal productsPrice) {
        return productsPrice.multiply(FEE_RATE);
    }
}
