package ru.yandex.practicum.telemetry.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.aggregator.configuration.KafkaSnapshotProducerConfig;

@Component
@Slf4j
@RequiredArgsConstructor
public class SnapshotProducer implements AutoCloseable {

    private final KafkaSnapshotProducerConfig config;
    private final KafkaTemplate<String, SensorsSnapshotAvro> kafkaTemplate;

    public void send(SensorsSnapshotAvro snapshot) {

        String topic = config.getTopicSnapshots();
        long timestamp = snapshot.getTimestamp().getEpochSecond();
        String hubId = snapshot.getHubId();

        kafkaTemplate.send(topic, null, timestamp, hubId, snapshot)
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
