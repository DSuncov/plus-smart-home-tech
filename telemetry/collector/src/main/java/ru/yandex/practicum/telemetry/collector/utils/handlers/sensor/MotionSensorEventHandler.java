package ru.yandex.practicum.telemetry.collector.utils.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
public class MotionSensorEventHandler implements BaseSensorEventHandler {

    @Override
    public SensorEventAvro mapToAvro(SensorEventProto sensorEvent) {
        MotionSensorProto motionSensorProto = sensorEvent.getMotionSensor();
        MotionSensorAvro payload = MotionSensorAvro.newBuilder()
                .setLinkQuality(motionSensorProto.getLinkQuality())
                .setMotion(motionSensorProto.getMotion())
                .setVoltage(motionSensorProto.getVoltage())
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
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }
}
