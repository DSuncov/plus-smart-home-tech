package ru.yandex.practicum.commerce.delivery.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.enums.DeliveryState;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "delivery")
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    UUID deliveryId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "country_from")),
            @AttributeOverride(name = "city", column = @Column(name = "city_from")),
            @AttributeOverride(name = "street", column = @Column(name = "street_from")),
            @AttributeOverride(name = "house", column = @Column(name = "house_from")),
            @AttributeOverride(name = "flat", column = @Column(name = "flat_from"))
    })
    Address addressFrom;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "country_to")),
            @AttributeOverride(name = "city", column = @Column(name = "city_to")),
            @AttributeOverride(name = "street", column = @Column(name = "street_to")),
            @AttributeOverride(name = "house", column = @Column(name = "house_to")),
            @AttributeOverride(name = "flat", column = @Column(name = "flat_to"))
    })
    Address addressTo;

    @Column(name = "order_id", nullable = false)
    UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    DeliveryState state;
}
