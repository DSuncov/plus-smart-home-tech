package ru.yandex.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioAction;

import java.util.Set;

public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, String> {

    void deleteByScenarioId(Long scenarioId);

    @Query("SELECT sa " +
            "FROM ScenarioAction sa " +
            "WHERE sa.scenario.id = :id")
    Set<ScenarioAction> findByScenarioId(Long id);
}
