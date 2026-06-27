package ru.yandex.practicum.commerce.feign.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.order.CreatedNewOrderRequest;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderOperations {

    @GetMapping
    ResponseEntity<List<OrderDto>> getOrdersByUser(@RequestParam @NotBlank String username);

    @PutMapping
    ResponseEntity<OrderDto> createOrder(@RequestBody @NotNull @Valid CreatedNewOrderRequest request);

    @PostMapping("/return")
    ResponseEntity<OrderDto> returnOrder(@RequestBody @NotNull @Valid ProductReturnRequest request);

    @PostMapping("/payment")
    ResponseEntity<OrderDto> successPayment(@RequestBody @NotNull UUID orderId);

    @PostMapping("/payment/failed")
    ResponseEntity<OrderDto> failedPayment(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery")
    ResponseEntity<OrderDto> successDeliveryOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery/failed")
    ResponseEntity<OrderDto> failedDeliveryOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/completed")
    ResponseEntity<OrderDto> completedOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/total")
    ResponseEntity<OrderDto> calculateTotalOrderCost(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/delivery")
    ResponseEntity<OrderDto> calculateDeliveryCost(@RequestBody @NotNull UUID orderId);

    @PostMapping("/assembly")
    ResponseEntity<OrderDto> successAssembly(@RequestBody @NotNull UUID orderId);

    @PostMapping("/assembly/failed")
    ResponseEntity<OrderDto> failedAssembly(@RequestBody @NotNull UUID orderId);
}
