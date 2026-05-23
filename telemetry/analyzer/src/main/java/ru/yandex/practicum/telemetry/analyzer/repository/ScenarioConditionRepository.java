package ru.yandex.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioCondition;

import java.util.Set;

public interface ScenarioConditionRepository extends JpaRepository<ScenarioCondition, String> {

    void deleteByScenarioId(Long scenarioId);

    @Query("SELECT sc " +
            "FROM ScenarioCondition sc " +
            "WHERE sc.scenario.id = :id")
    Set<ScenarioCondition> findByScenarioId(Long id);
}
