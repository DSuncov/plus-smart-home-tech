package ru.yandex.practicum.commerce.store.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.commerce.utils.enums.ProductCategory;
import ru.yandex.practicum.commerce.utils.enums.ProductState;
import ru.yandex.practicum.commerce.utils.enums.QuantityState;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Product {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "product_id", nullable = false)
    UUID productId;

    @Column(name = "product_name", nullable = false)
    String productName;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "image_src")
    String imageSrc;

    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state", nullable = false)
    QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_state", nullable = false)
    ProductState productState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", nullable = false)
    ProductCategory productCategory;

    @Column(name = "price", nullable = false)
    Double price;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Product product = (Product) object;
        return Objects.equals(productId, product.productId)
                && Objects.equals(productName, product.productName)
                && Objects.equals(description, product.description)
                && Objects.equals(imageSrc, product.imageSrc)
                && quantityState == product.quantityState
                && productState == product.productState
                && productCategory == product.productCategory
                && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, description, imageSrc, quantityState, productState, productCategory, price);
    }
}
