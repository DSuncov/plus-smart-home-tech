package ru.yandex.practicum.commerce.order.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.enums.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    UUID orderId;

    @Column(name = "shopping_cart_id", nullable = false)
    UUID shoppingCartId;

    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Long> products;

    @Column(name = "payment_id")
    UUID paymentId;

    @Column(name = "delivery_id")
    UUID deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    OrderState state;

    @Column(name = "weight")
    Double deliveryWeight;

    @Column(name = "volume")
    Double deliveryVolume;

    @Column(name = "fragile")
    Boolean fragile;

    @Column(name = "total_price")
    BigDecimal totalPrice;

    @Column(name = "delivery_price")
    BigDecimal deliveryPrice;

    @Column(name = "products_price")
    BigDecimal productsPrice;

    @Column(name = "username", nullable = false)
    String username;
}
