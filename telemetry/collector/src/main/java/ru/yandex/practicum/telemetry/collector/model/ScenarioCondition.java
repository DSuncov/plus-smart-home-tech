package ru.yandex.practicum.telemetry.collector.model;

import lombok.Getter;

@Getter
public class ScenarioCondition {
    String sensorId;
    String type;
    String operation;
    Integer value;
}