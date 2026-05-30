package ru.yandex.practicum.telemetry.analyzer.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(KafkaAnalyzerProperties.class)
@RequiredArgsConstructor
@Getter
public class SnapshotKafkaConsumerConfig {

    private final KafkaAnalyzerProperties kafkaProperties;

    @Bean("snapshotConsumerFactory")
    public Properties snapshotConsumerFactory() {
        KafkaAnalyzerProperties.ConsumerGroup config = kafkaProperties.snapshotConsumer();
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.keyDeserializer());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.enableAutoCommit());
        properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, kafkaProperties.fetchMaxWaitMs());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, config.valueDeserializer());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, config.groupId());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.autoOffsetReset());
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, config.maxPollRecords());
        return properties;
    }

    @Bean
    public KafkaConsumer<String, SensorsSnapshotAvro> kafkaSnapshotConsumer() {
        Properties properties = snapshotConsumerFactory();
        return new KafkaConsumer<>(properties);
    }
}
