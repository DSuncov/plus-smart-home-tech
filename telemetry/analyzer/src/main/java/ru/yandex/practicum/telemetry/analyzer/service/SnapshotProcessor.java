package ru.yandex.practicum.telemetry.analyzer.service;

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
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.configuration.SnapshotKafkaConsumerConfig;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor implements Runnable {

    private final SnapshotKafkaConsumerConfig config;
    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final Map<TopicPartition, OffsetAndMetadata> offsets = new ConcurrentHashMap<>();
    private final SnapshotDbHandler handler;

    @Override
    @KafkaListener(topics = "telemetry.snapshots.v1", groupId = "analyzer-snapshot-group")
    public void run() {
        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(config.getKafkaProperties().snapshotConsumer().topic()));

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> data = consumer.poll(Duration.ofMillis(500));
                log.info("ПОЛУЧИЛИ ЗАПИСИ СНАПШОТА");
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : data) {
                    SensorsSnapshotAvro avro = record.value();
                    if (avro != null) {
                        offsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));
                        log.info("ОТПРАВЛЯЕМ НА ОБРАБОТКУ");
                        handler.typeHandler(avro);
                        log.info("ВЫПОЛНИЛИ ОБРАБОТКУ");
                    }
                }

                if (!data.isEmpty()) {
                    consumer.commitSync(offsets);
                }
            }
        } catch (WakeupException ignored) {
//        } catch (Exception e) {
//            log.error("Ошибка во время обработки событий от датчиков", e);
//        } finally {
//            try {
//                consumer.commitSync(offsets);
//            } catch (Exception e) {
//                log.error("Смещения не зафиксированы");
//            } finally {
//                log.info("Закрываем консьюмер");
//                consumer.close();
//            }
        }
    }
}
