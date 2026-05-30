package ru.yandex.practicum.telemetry.aggregator.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(KafkaAggregatorProperties.class)
@RequiredArgsConstructor
@Getter
public class KafkaSnapshotProducerConfig {

    private final KafkaAggregatorProperties kafkaProperties;

    public ProducerFactory<String, SensorsSnapshotAvro> producerFactory() {
        KafkaAggregatorProperties.ProducerGroup config = kafkaProperties.producer();
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.keySerializer());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.valueSerializer());

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, SensorsSnapshotAvro> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
