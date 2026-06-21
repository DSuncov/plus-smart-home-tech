package ru.yandex.practicum.commerce.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.order.CreatedNewOrderRequest;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.enums.DeliveryState;
import ru.yandex.practicum.commerce.enums.OrderState;
import ru.yandex.practicum.commerce.exception.ConflictException;
import ru.yandex.practicum.commerce.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.feign.delivery.DeliveryClient;
import ru.yandex.practicum.commerce.feign.payment.PaymentClient;
import ru.yandex.practicum.commerce.feign.store.ShoppingStoreClient;
import ru.yandex.practicum.commerce.feign.warehouse.WarehouseClient;
import ru.yandex.practicum.commerce.order.mapper.OrderMapper;
import ru.yandex.practicum.commerce.order.model.Order;
import ru.yandex.practicum.commerce.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;
    private final WarehouseClient warehouseClient;

    public List<OrderDto> getOrdersByUser(String username) {
        log.info("Получаем список заказов для пользователя: {}", username);
        List<Order> ordersByUser = repository.findByUsername(username);

        return ordersByUser.stream()
                .map(mapper::toDto)
                .toList();
    }

    public OrderDto createOrder(CreatedNewOrderRequest request) {

        ShoppingCartDto shoppingCartDto = request.shoppingCart();

        BookedProductsDto bookedProductsDto = warehouseClient.checkQuantityProductInWarehouse(shoppingCartDto.products()).getBody();

        Order newOrder = Order.builder()
                .shoppingCartId(shoppingCartDto.shoppingCartId())
                .products(shoppingCartDto.products())
                .fragile(bookedProductsDto.fragile())
                .deliveryWeight(bookedProductsDto.deliveryWeight())
                .deliveryVolume(bookedProductsDto.deliveryVolume())
                .state(OrderState.NEW)
                .build();

        Order createdOrder = repository.save(newOrder);

        AddressDto from = warehouseClient.getWarehouseAddress().getBody();
        AddressDto to = request.deliveryAddress();

        DeliveryDto deliveryDto = new DeliveryDto(
                null,
                from,
                to,
                createdOrder.getOrderId(),
                DeliveryState.CREATED);

        UUID deliveryId = Objects.requireNonNull(deliveryClient.createNewDelivery(deliveryDto).getBody()).deliveryId();
        newOrder.setDeliveryId(deliveryId);

        createdOrder = repository.save(newOrder);
        return mapper.toDto(createdOrder);
    }

    public OrderDto returnOrder(ProductReturnRequest request) {
        log.info("Выполняем возврат товаров на склад.");
        Order order = repository.findById(request.orderId()).orElseThrow(() -> new NoOrderFoundException("Заказ для платежа не найден."));

        warehouseClient.returnToWarehouse(request);
        order.setState(OrderState.PRODUCT_RETURNED);
        Order updatedOrder = repository.save(order);

        // Нужно поменять статус товара в Store в зависимости от количества

        log.info("Товары успешно возвращены на склад.");
        return mapper.toDto(updatedOrder);
    }

    public OrderDto successPayment(UUID paymentId) {
        log.info("Устанавливаем статус заказа на 'Оплачен'");
        Order order = repository.findByPaymentId(paymentId).orElseThrow(() -> new NoOrderFoundException("Заказ для платежа не найден."));

        if (order.getState().equals(OrderState.PAID)) {
            throw new ConflictException("Заказ уже оплачен.");
        }

        if (order.getState().equals(OrderState.ON_PAYMENT)) {
            order.setState(OrderState.PAID);
            return mapper.toDto(order);
        }

        PaymentDto paymentDto = paymentClient.createPayment(mapper.toDto(order)).getBody();
        order.setPaymentId(paymentDto.paymentId());
        order.setState(OrderState.ON_PAYMENT);

        Order updatedOrder = repository.save(order);

        log.info("Статус заказа успешно изменен.");
        return mapper.toDto(updatedOrder);
    }

    public OrderDto failedPayment(UUID paymentId) {
        log.info("Устанавливаем статус заказа на 'Неудачная оплата'.");
        Order order = repository.findByPaymentId(paymentId).orElseThrow(() -> new NoOrderFoundException("Заказ для платежа не найден."));

        order.setState(OrderState.PAYMENT_FAILED);
        Order updatedOrder = repository.save(order);

        log.info("Статус заказа успешно изменен.");
        return mapper.toDto(updatedOrder);
    }

    public OrderDto successDeliveryOrder(UUID orderId) {
        log.info("Устанавливаем статус заказа на 'Доставлен'.");
        Order order = repository.findById(orderId).orElseThrow(() -> new NoOrderFoundException("Заказ для платежа не найден."));

        order.setState(OrderState.DELIVERED);
        Order updatedOrder = repository.save(order);

        log.info("Статус заказа успешно изменен.");
        return mapper.toDto(updatedOrder);
    }

    public OrderDto failedDeliveryOrder(UUID orderId) {
        log.info("Устанавливаем статус заказа на 'Неудачная доставка'.");
        Order order = repository.findById(orderId).orElseThrow(() -> new NoOrderFoundException("Заказ для платежа не найден."));

        order.setState(OrderState.DELIVERY_FAILED);
        Order updatedOrder = repository.save(order);

        log.info("Статус заказа успешно изменен.");
        return mapper.toDto(updatedOrder);
    }

    public OrderDto completedOrder(UUID orderId) {
        log.info("Устанавливаем статус заказа на 'Завершен'.");
        Order order = repository.findById(orderId).orElseThrow(() -> new NoOrderFoundException("Заказ для платежа не найден."));

        order.setState(OrderState.COMPLETED);
        Order updatedOrder = repository.save(order);

        log.info("Статус заказа успешно изменен.");
        return mapper.toDto(updatedOrder);
    }

    public OrderDto calculateTotalOrderCost(UUID orderId) {
        log.info("Выполняем расчет полной стоимости заказа.");
        Order order = repository.findById(orderId).orElseThrow(() -> new NoOrderFoundException("Заказ для платежа не найден."));

        BigDecimal productCost = paymentClient.calculateProductCost(order.getProducts()).getBody();
        order.setProductsPrice(productCost);

        BigDecimal totalCost = paymentClient.calculateTotalCost(mapper.toDto(order)).getBody();
        order.setTotalPrice(totalCost);
        Order updatedOrder = repository.save(order);

        log.info("Полная стоимость заказа успешно рассчитана.");
        return mapper.toDto(updatedOrder);
    }

    public OrderDto calculateDeliveryCost(UUID orderId) {
        log.info("Выполняем расчет стоимости доставки.");
        Order order = repository.findById(orderId).orElseThrow(() -> new NoOrderFoundException("Заказ для платежа не найден."));

        BigDecimal deliveryCost = deliveryClient.deliveryCost(mapper.toDto(order));
        order.setDeliveryPrice(deliveryCost);

        Order updatedOrder = repository.save(order);

        log.info("Стоимость доставки успешно рассчитана.");
        return mapper.toDto(updatedOrder);
    }

    public OrderDto successAssembly(UUID orderId) {
        log.info("Отправляем запрос на сборку заказа.");
        Order order = repository.findById(orderId).orElseThrow(() -> new NoOrderFoundException("Заказ для платежа не найден."));

        if (order.getState().equals(OrderState.ASSEMBLED)) {
            throw new ConflictException("Заказ уже в сборке.");
        }

        if (order.getState().equals(OrderState.CANCELED)
                || order.getState().equals(OrderState.PRODUCT_RETURNED)
                || order.getState().equals(OrderState.COMPLETED)
        ) {
            throw new ConflictException("Заказ нельзя собрать для этих статусов");
        }

        AssemblyProductsForOrderRequest request = new AssemblyProductsForOrderRequest(order.getOrderId(), order.getProducts());

        BookedProductsDto bookedProductsDto = warehouseClient.assemblyProductForOrderFromShoppingCart(request).getBody();

        order.setState(OrderState.ASSEMBLED);
        order.setDeliveryWeight(bookedProductsDto.deliveryWeight());
        order.setDeliveryVolume(bookedProductsDto.deliveryVolume());
        order.setFragile(bookedProductsDto.fragile());

        Order updatedOrder = repository.save(order);

        log.info("Сборка заказа успешно завершена.");
        return mapper.toDto(updatedOrder);
    }

    public OrderDto failedAssembly(UUID orderId) {
        Order order = repository.findById(orderId).orElseThrow(() -> new NoOrderFoundException("Заказ для платежа не найден."));

        order.setState(OrderState.ASSEMBLY_FAILED);

        Order updatedOrder = repository.save(order);
        return mapper.toDto(updatedOrder);
    }
}
