package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sensors")
public class Sensor {

    @Id
    @Column(name = "id")
    String id;

    @Column(name = "hub_id", nullable = false)
    @NotBlank
    String hubId;
}
