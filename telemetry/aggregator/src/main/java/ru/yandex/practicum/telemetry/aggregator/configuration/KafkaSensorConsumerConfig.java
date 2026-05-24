package ru.yandex.practicum.telemetry.aggregator.configuration;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.Properties;

@Configuration
@Getter
public class KafkaSensorConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;

    @Value("${spring.kafka.consumer.topic.sensors}")
    private String topicSensors;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.properties.fetch.min.bytes}")
    private Integer fetchMinBytes;

    @Value("${spring.kafka.consumer.properties.max.poll.records}")
    private Integer maxPollRecords;

    @Value("${spring.kafka.consumer.properties.enable.auto.commit}")
    private Boolean enableAutoCommit;

    @Value("${spring.kafka.consumer.properties.fetch.max.wait.ms}")
    private Integer fetchMaxWaitMs;

    @Value("${spring.kafka.consumer.properties.max.partition.fetch.bytes}")
    private Integer maxPartitionFetchBytes;

    @Bean
    public Properties consumerFactory() {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        config.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, fetchMinBytes);
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        config.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWaitMs);
        config.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);

        return config;
    }

    @Bean
    public KafkaConsumer<String, SensorEventAvro> kafkaConsumer() {
        Properties properties = consumerFactory();
        return new KafkaConsumer<>(properties);
    }
}
