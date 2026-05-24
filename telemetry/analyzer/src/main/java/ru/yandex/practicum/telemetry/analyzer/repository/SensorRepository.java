package ru.yandex.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;

import java.util.List;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

    Optional<Sensor> findByIdAndHubId(String id, String hubId);

    List<Sensor> findByHubId(String hubId);

    void deleteByIdAndHubId(String id, String hubId);
}
