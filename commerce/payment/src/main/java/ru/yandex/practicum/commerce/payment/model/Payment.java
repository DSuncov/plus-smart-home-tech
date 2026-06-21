package ru.yandex.practicum.commerce.payment.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.enums.PaymentState;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    UUID paymentId;

    @Column(name = "order_id", nullable = false)
    UUID orderId;

    @Column(name = "product_total")
    BigDecimal productTotal;

    @Column(name = "total_payment")
    BigDecimal totalPayment;

    @Column(name = "delivery_total")
    BigDecimal deliveryTotal;

    @Column(name = "fee_total")
    BigDecimal feeTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    PaymentState state;
}
