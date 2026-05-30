package ru.yandex.practicum.telemetry.aggregator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.aggregator.configuration.KafkaSensorConsumerConfig;
import ru.yandex.practicum.telemetry.aggregator.service.SnapshotHandler;
import ru.yandex.practicum.telemetry.aggregator.service.SnapshotProducer;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class AggregationStarter {

    private final KafkaSensorConsumerConfig config;
    private final SnapshotHandler handler;
    private final SnapshotProducer producer;
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final Map<TopicPartition, OffsetAndMetadata> offsets = new ConcurrentHashMap<>();

    @KafkaListener(topics = "telemetry.sensors.v1", groupId = "aggregator-group")
    public void start() {

        try (consumer) {

            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

            consumer.subscribe(List.of(config.getTopicSensors()));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> data = consumer.poll(Duration.ofMillis(config.getFetchMaxWaitMs()));

                for (ConsumerRecord<String, SensorEventAvro> record : data) {
                    SensorEventAvro sensorEventAvro = record.value();

                    handler.updateState(sensorEventAvro).ifPresent(snapshot -> {
                        producer.send(snapshot);
                        offsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));
                    });
                }
            }

        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                consumer.commitSync(offsets);
            } catch (Exception e) {
                log.error("Смещения не зафиксированы");
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }
}
