package ru.yandex.practicum.telemetry.collector.utils.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

import java.time.Instant;

@Component
public class SwitchSensorEventHandler implements BaseSensorEventHandler {

    @Override
    public SensorEventAvro mapToAvro(SensorEventProto sensorEvent) {
        SwitchSensorAvro payload =  SwitchSensorAvro.newBuilder()
                .setState(sensorEvent.getSwitchSensor().getState())
                .build();

        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        sensorEvent.getTimestamp().getSeconds(),
                        sensorEvent.getTimestamp().getNanos()
                ))
                .setPayload(payload)
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }
}
