package ru.yandex.practicum.telemetry.collector.service.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.collector.model.enums.SensorEventType;
import ru.yandex.practicum.telemetry.collector.model.sensors.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.SwitchSensorEvent;

@Component
public class SwitchSensorEventHandler implements BaseSensorEventHandler {

    @Override
    public SensorEventAvro mapToAvro(SensorEvent sensorEvent) {
        SwitchSensorEvent event = (SwitchSensorEvent) sensorEvent;
        SwitchSensorAvro payload =  SwitchSensorAvro.newBuilder()
                .setState(event.getState())
                .build();

        return SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
