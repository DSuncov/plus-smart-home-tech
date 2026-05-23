package ru.yandex.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;

import java.util.Optional;
import java.util.Set;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    Optional<Scenario> findByNameAndHubId(String name, String hubId);

    @Query("SELECT DISTINCT s FROM Scenario s " +
            "LEFT JOIN FETCH s.conditions sc " +
            "WHERE s.hubId = :hubId")
    Set<Scenario> findByHubIdWithConditions(String hubId);

    @Query("SELECT DISTINCT s FROM Scenario s " +
            "LEFT JOIN FETCH s.actions sa " +
            "WHERE s.hubId = :hubId")
    Set<Scenario> findByHubIdWithActions(String hubId);
}
