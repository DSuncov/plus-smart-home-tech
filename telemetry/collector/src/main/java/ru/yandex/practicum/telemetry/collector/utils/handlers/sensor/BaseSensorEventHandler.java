package ru.yandex.practicum.telemetry.collector.utils.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public interface BaseSensorEventHandler {

    SensorEventAvro mapToAvro(SensorEventProto event);

    SensorEventProto.PayloadCase getType();
}
