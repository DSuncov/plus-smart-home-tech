package ru.yandex.practicum.telemetry.analyzer.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(
        String bootstrapServers,
        String keyDeserializer,
        Boolean enableAutoCommit,

        ConsumerGroup hubConsumer,
        ConsumerGroup snapshotConsumer
) {
    public record ConsumerGroup(
            String topic,
            String groupId,
            String valueDeserializer,
            String autoOffsetReset,
            Integer maxPollRecords
    ) {}
}
