package ru.yandex.practicum.telemetry.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
@Slf4j
public class SnapshotHandler {

    private final Map<String, SensorsSnapshotAvro> snapshots = new ConcurrentHashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro currentSnapshot = snapshots.computeIfAbsent(event.getHubId(), hubId ->
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(hubId)
                        .setSensorsState(new HashMap<>())
                        .setTimestamp(event.getTimestamp())
                        .build()
        );

        SensorStateAvro oldState = currentSnapshot.getSensorsState().get(event.getId());

        if (oldState != null &&
                (oldState.getTimestamp().isAfter(event.getTimestamp()) || oldState.getData().equals(event.getPayload()))) {
            return Optional.empty();
        }

        SensorStateAvro newSensorState = SensorStateAvro.newBuilder()
                .setData(event.getPayload())
                .setTimestamp(event.getTimestamp())
                .build();

        currentSnapshot.getSensorsState().put(event.getId(), newSensorState);
        currentSnapshot.setTimestamp(event.getTimestamp());

        return Optional.of(currentSnapshot);
    }
}
