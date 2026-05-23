package ru.yandex.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;

import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

    Optional<Sensor> findByIdAndHubId(String id, String hubId);

    void deleteByIdAndHubId(String id, String hubId);

    @Query("SELECT s " +
            "FROM Sensor s " +
            "WHERE s.id = :sensorId")
    Optional<Sensor> findBySensorId(String sensorId);
}
