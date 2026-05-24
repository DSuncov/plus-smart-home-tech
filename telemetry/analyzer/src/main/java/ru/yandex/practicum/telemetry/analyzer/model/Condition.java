package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.telemetry.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.telemetry.analyzer.model.enums.Operation;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conditions")
@Builder
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotNull
    ConditionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation", nullable = false)
    @NotNull
    Operation operation;

    @Column(name = "value", nullable = false)
    @NotNull
    Integer value;
}
