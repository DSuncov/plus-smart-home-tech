package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_booking")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderBooking {

    @Id
    @Column(name = "order_id", nullable = false)
    UUID orderId;

    @Column(name = "delivery_id")
    UUID deliveryId;

    @ElementCollection
    @CollectionTable(name = "order_booking_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    Map<UUID, Long> products = new HashMap<>();
}
