package ru.yandex.practicum.commerce.warehouse.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.warehouse.model.OrderBooking;

import java.util.Optional;
import java.util.UUID;

public interface OrderBookingRepository extends JpaRepository<OrderBooking, UUID> {

    Optional<OrderBooking> findByOrderId(@NotNull UUID orderId);
}
