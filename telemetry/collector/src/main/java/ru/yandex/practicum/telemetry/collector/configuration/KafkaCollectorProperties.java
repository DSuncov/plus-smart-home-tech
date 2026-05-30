package ru.yandex.practicum.telemetry.collector.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaCollectorProperties(
        String bootstrapServers,
        String keySerializer,
        String valueSerializer,
        String topicHubs,
        String topicSensors
) {}
