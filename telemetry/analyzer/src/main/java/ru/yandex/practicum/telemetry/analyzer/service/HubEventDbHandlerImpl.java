package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.model.*;
import ru.yandex.practicum.telemetry.analyzer.model.enums.ActionType;
import ru.yandex.practicum.telemetry.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.telemetry.analyzer.model.enums.Operation;
import ru.yandex.practicum.telemetry.analyzer.repository.*;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HubEventDbHandlerImpl implements HubEventDbHandler {

    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;
    private final ScenarioConditionRepository scenarioConditionRepository;
    private final ScenarioActionRepository scenarioActionRepository;

    @Override
    public void typeHandler(HubEventAvro avro) {
        String hubId = avro.getHubId();
        switch (avro.getPayload()) {
            case DeviceAddedEventAvro deviceAddedEventAvro -> deviceAdded(deviceAddedEventAvro, hubId);
            case DeviceRemovedEventAvro deviceRemovedEventAvro -> deviceRemoved(deviceRemovedEventAvro, hubId);
            case ScenarioAddedEventAvro scenarioAddedEventAvro -> scenarioAdded(scenarioAddedEventAvro, hubId);
            case ScenarioRemovedEventAvro scenarioRemovedEventAvro -> scenarioRemoved(scenarioRemovedEventAvro, hubId);
            default -> throw new IllegalStateException("Неизвестное значение: " + avro.getPayload());
        }
    }

    @Transactional
    private void deviceAdded(DeviceAddedEventAvro avro, String hubId) {
        sensorRepository.findByIdAndHubId(avro.getId(), hubId).ifPresentOrElse(
                sensor -> log.info("Датчик с id = {} и hubId = {} уже сохранены", avro.getId(), hubId),
                () -> sensorRepository.save(new Sensor(avro.getId(), hubId))
        );
    }

    @Transactional
    private void deviceRemoved(DeviceRemovedEventAvro avro, String hubId) {
        sensorRepository.findByIdAndHubId(avro.getId(), hubId).ifPresentOrElse(
                sensor -> sensorRepository.deleteByIdAndHubId(avro.getId(), hubId),
                () -> log.info("Датчик с id = {} и hubId = {} отсутствует", avro.getId(), hubId)
        );
    }

    @Transactional
    private void scenarioAdded(ScenarioAddedEventAvro avro, String hubId) {
        Scenario scenario = scenarioRepository.findByNameAndHubId(avro.getName(), hubId)
                .orElseGet(() -> scenarioRepository.save(Scenario.builder().hubId(hubId).name(avro.getName()).build()));

        List<ScenarioCondition> conditions = avro.getConditions().stream()
                .map(c -> {
                    Condition condition = conditionRepository.save(Condition.builder()
                            .type(ConditionType.valueOf(c.getType().name()))
                            .operation(Operation.valueOf(c.getOperation().name()))
                            .value(convertFromAvroUnionValue(c.getValue()))
                            .build());
                    return ScenarioCondition.builder()
                            .id(ScenarioConditionKey.builder()
                                    .scenarioId(scenario.getId())
                                    .conditionId(condition.getId())
                                    .sensorId(c.getSensorId())
                                    .build())
                            .scenario(scenario)
                            .sensor(sensorRepository.getReferenceById(Long.valueOf(c.getSensorId())))
                            .condition(condition)
                            .build();
                })
                .toList();

        scenarioConditionRepository.saveAll(conditions);

        List<ScenarioAction> actions = avro.getActions().stream()
                .map(a -> {
                    Action action = actionRepository.save(Action.builder()
                            .type(ActionType.valueOf(a.getType().name()))
                            .value(a.getValue())
                            .build());

                    return ScenarioAction.builder()
                            .id(ScenarioActionKey.builder()
                                    .scenarioId(scenario.getId())
                                    .actionId(action.getId())
                                    .sensorId(a.getSensorId())
                                    .build())
                            .scenario(scenario)
                            .sensor(sensorRepository.getReferenceById(Long.valueOf(a.getSensorId())))
                            .action(action)
                            .build();
                })
                .toList();

        scenarioActionRepository.saveAll(actions);
    }

    @Transactional
    private void scenarioRemoved(ScenarioRemovedEventAvro avro, String hubId) {
        scenarioRepository.findByNameAndHubId(avro.getName(), hubId).ifPresentOrElse(scenario -> {
                    scenarioRepository.delete(scenario);
                    scenarioConditionRepository.deleteByScenarioId(scenario.getId());
                    scenarioActionRepository.deleteByScenarioId(scenario.getId());
                },
                () -> log.info("Сценарий с id = {} и hubId = {} отсутствует", avro.getName(), hubId)
        );
    }

    private Integer convertFromAvroUnionValue(Object value) {
        return switch (value) {
            case Integer integer -> integer;
            case Boolean bool -> bool ? 1 : 0;
            case null, default -> null;
        };
    }
}
