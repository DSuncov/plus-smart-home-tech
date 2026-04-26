package ru.yandex.practicum.telemetry.collector.model;

import lombok.Getter;

@Getter
public class DeviceAction {
    String sensorId;
    String type;
    Integer value;
}
