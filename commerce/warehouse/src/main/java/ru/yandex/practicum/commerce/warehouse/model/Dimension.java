package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class Dimension {

    @Column(name = "width", nullable = false)
    Double width;

    @Column(name = "height", nullable = false)
    Double height;

    @Column(name = "depth", nullable = false)
    Double depth;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Dimension dimension = (Dimension) object;
        return Objects.equals(width, dimension.width) && Objects.equals(height, dimension.height) && Objects.equals(depth, dimension.depth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, depth);
    }
}
