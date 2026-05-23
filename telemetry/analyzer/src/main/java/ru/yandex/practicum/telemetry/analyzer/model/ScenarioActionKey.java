package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
public class ScenarioActionKey {
    Long scenarioId;
    String sensorId;
    Long actionId;
}
