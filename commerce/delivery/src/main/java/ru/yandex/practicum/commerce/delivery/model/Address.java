package ru.yandex.practicum.commerce.delivery.model;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Address {

    String country;
    String city;
    String street;
    String house;
    String flat;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Address address = (Address) object;
        return Objects.equals(country, address.country)
                && Objects.equals(city, address.city)
                && Objects.equals(street, address.street)
                && Objects.equals(house, address.house)
                && Objects.equals(flat, address.flat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, street, house, flat);
    }
}
