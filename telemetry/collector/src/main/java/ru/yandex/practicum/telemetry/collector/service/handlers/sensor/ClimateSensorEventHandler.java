package ru.yandex.practicum.telemetry.collector.service.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
public class ClimateSensorEventHandler implements BaseSensorEventHandler {

    @Override
    public SensorEventAvro mapToAvro(SensorEventProto sensorEvent) {
        ClimateSensorAvro payload = ClimateSensorAvro.newBuilder()
                .setTemperatureC(sensorEvent.getClimateSensor().getTemperatureC())
                .setHumidity(sensorEvent.getClimateSensor().getHumidity())
                .setCo2Level(sensorEvent.getClimateSensor().getCo2Level())
                .build();

        return SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(Instant.ofEpochSecond(
                        sensorEvent.getTimestamp().getSeconds(),
                        sensorEvent.getTimestamp().getNanos()
                ))
                .setPayload(payload)
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }
}
