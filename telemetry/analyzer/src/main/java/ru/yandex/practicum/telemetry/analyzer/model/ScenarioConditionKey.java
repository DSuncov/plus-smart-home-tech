package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
public class ScenarioConditionKey {
    @Column(name = "scenario_id")
    Long scenarioId;

    @Column(name = "sensor_id")
    String sensorId;

    @Column(name = "condition_id")
    Long conditionId;
}
