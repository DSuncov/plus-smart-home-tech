package ru.yandex.practicum.telemetry.analyzer.service;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotDbHandler {
    void typeHandler(SensorsSnapshotAvro avro);
}
