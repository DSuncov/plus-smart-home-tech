package ru.yandex.practicum.commerce.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.commerce.delivery.model.Delivery;
import ru.yandex.practicum.commerce.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.delivery.ShippedRequest;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.enums.DeliveryState;
import ru.yandex.practicum.commerce.exception.DeliveryAlreadyExistException;
import ru.yandex.practicum.commerce.exception.NoDeliveryFoundException;
import ru.yandex.practicum.commerce.feign.order.OrderClient;
import ru.yandex.practicum.commerce.feign.warehouse.WarehouseClient;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository repository;
    private final DeliveryMapper mapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Transactional
    public DeliveryDto createNewDelivery(DeliveryDto request) {
        log.info("Проверяем наличие доставки с id = {} в БД.", request.deliveryId());
        boolean deliveryIsExist = repository.existsById(request.deliveryId());

        if (deliveryIsExist) {
            throw new DeliveryAlreadyExistException("Доставка с таким id уже существует.");
        }

        log.info("Доставка с id = {} не найдена. Создаем новую.", request.deliveryId());
        Delivery delivery = repository.save(mapper.toEntity(request));
        log.info("Доставка успешно создана. Передаем данные кклиенту.");
        return mapper.toDto(delivery);
    }

    @Transactional
    public void successfulDelivery(UUID orderId) {
        log.info("Проверяем наличие доставки для заказа с id = {}.", orderId);
        Delivery delivery = repository.findByOrderId(orderId).orElseThrow(() -> new NoDeliveryFoundException("Доставка для заказа не найдена."));
        delivery.setState(DeliveryState.DELIVERED);

        repository.save(delivery);
        orderClient.successDeliveryOrder(orderId);
    }

    @Transactional
    public void pickedDelivery(UUID orderId) {
        log.info("Проверяем наличие доставки для заказа с id = {}.", orderId);
        Delivery delivery = repository.findByOrderId(orderId).orElseThrow(() -> new NoDeliveryFoundException("Доставка для заказа не найдена."));
        delivery.setState(DeliveryState.IN_PROGRESS);

        repository.save(delivery);
        orderClient.successAssembly(orderId);

        warehouseClient.shipped(new ShippedRequest(orderId, delivery.getDeliveryId()));
    }

    @Transactional
    public void deliveryFailed(UUID orderId) {
        log.info("Проверяем наличие доставки для заказа с id = {}.", orderId);
        Delivery delivery = repository.findByOrderId(orderId).orElseThrow(() -> new NoDeliveryFoundException("Доставка для заказа не найдена."));
        delivery.setState(DeliveryState.FAILED);

        repository.save(delivery);
        orderClient.failedDeliveryOrder(orderId);
    }

    @Transactional
    public BigDecimal calculateDeliveryTotalCost(OrderDto request) {
        log.info("Проверяем наличие доставки для заказа с id = {}.", request.orderId());
        Delivery delivery = repository.findByOrderId(request.orderId()).orElseThrow(() -> new NoDeliveryFoundException("Доставка для заказа не найдена."));

        AddressDto warehouseAddress = warehouseClient.getWarehouseAddress().getBody();

        BigDecimal baseDeliveryPrice = BigDecimal.valueOf(5);
        BigDecimal totalDeliveryCost = baseDeliveryPrice;

        log.info("Выполняем расчет стоимости доставки.");
        log.info("Определяем коэффициент в зависимости от склада.");

        BigDecimal warehouseRatio = BigDecimal.valueOf(warehouseAddress.country().contains("ADDRESS_1") ? 1.0 : 2.0);
        totalDeliveryCost = totalDeliveryCost.add(baseDeliveryPrice.multiply(warehouseRatio));

        log.info("Определяем коэффициент в зависимости от хрупкости.");
        BigDecimal fragileRatio = BigDecimal.valueOf(request.fragile() ? 0.2 : 1.0);
        totalDeliveryCost = totalDeliveryCost.add(totalDeliveryCost.multiply(fragileRatio));

        log.info("Определяем коэффициент в зависимости от веса заказа.");
        BigDecimal weighCost = BigDecimal.valueOf(0.3).multiply(BigDecimal.valueOf(request.deliveryWeight()));
        totalDeliveryCost = totalDeliveryCost.add(weighCost);

        log.info("Определяем коэффициент в зависимости от объема заказа.");
        BigDecimal volumeCost = BigDecimal.valueOf(0.2).multiply(BigDecimal.valueOf(request.deliveryVolume()));
        totalDeliveryCost = totalDeliveryCost.add(volumeCost);

        if (warehouseAddress.street().equals(delivery.getAddressTo().getStreet())) {
            totalDeliveryCost = totalDeliveryCost.add(totalDeliveryCost.multiply(BigDecimal.valueOf(0.2)));
        }

        return totalDeliveryCost;
    }
}
