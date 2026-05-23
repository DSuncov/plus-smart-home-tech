package ru.yandex.practicum.telemetry.collector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.configuration.KafkaEventProducerConfig;
import ru.yandex.practicum.telemetry.collector.handlers.hub.BaseHubEventHandler;
import ru.yandex.practicum.telemetry.collector.handlers.sensor.BaseSensorEventHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventProducer implements AutoCloseable {

    private final KafkaEventProducerConfig config;
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

    public void send(SensorEventProto event, BaseSensorEventHandler sensorEventHandler) {
        SensorEventAvro sensorEventAvro = sensorEventHandler.mapToAvro(event);

        String topic = config.getTopicSensors();
        long timestamp = event.getTimestamp().getSeconds();
        String hubId = sensorEventAvro.getHubId();

        kafkaTemplate.send(topic, null, timestamp, hubId, sensorEventAvro)
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.info("Сообщение успешно отправлено");
                    } else {
                        log.error("Сообщение не удалось отправить");
                    }
                });
    }

    public void send(HubEventProto event, BaseHubEventHandler baseHubEventHandler) {
        HubEventAvro hubEventAvro = baseHubEventHandler.mapToAvro(event);

        String topic = config.getTopicHubs();
        long timestamp = event.getTimestamp().getSeconds();
        String hubId = hubEventAvro.getHubId();

        kafkaTemplate.send(topic, null, timestamp, hubId, hubEventAvro)
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.info("Сообщение успешно отправлено");
                    } else {
                        log.error("Сообщение не удалось отправить");
                    }
                });
    }

    @Override
    public void close() {
        kafkaTemplate.flush();
    }
}
