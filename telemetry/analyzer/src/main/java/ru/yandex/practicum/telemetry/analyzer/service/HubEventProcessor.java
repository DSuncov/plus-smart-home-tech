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
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.configuration.HubKafkaConsumerConfig;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {

    private final HubKafkaConsumerConfig config;
    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final Map<TopicPartition, OffsetAndMetadata> offsets = new ConcurrentHashMap<>();
    private final HubEventDbHandlerImpl handler;

    private static final Integer OFFSET_INCREMENT = 1;

    @Override
    @KafkaListener(topics = "telemetry.hubs.v1", groupId = "analyzer-hub-group")
    public void run() {
        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(config.getKafkaProperties().hubConsumer().topic()));

            while (true) {
                ConsumerRecords<String, HubEventAvro> data = consumer.poll(Duration.ofMillis(config.getKafkaProperties().fetchMaxWaitMs()));
                for (ConsumerRecord<String, HubEventAvro> record : data) {
                    HubEventAvro avro = record.value();

                    if (avro != null) {
                        offsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + OFFSET_INCREMENT));
                        handler.handler(avro);
                    }
                }

                if (!data.isEmpty()) {
                    consumer.commitSync(offsets);
                }
            }
        } catch (WakeupException ignored) {}
    }
}
