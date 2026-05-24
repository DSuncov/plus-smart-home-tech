package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.client.HubRouterClient;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioCondition;
import ru.yandex.practicum.telemetry.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.telemetry.analyzer.model.enums.Operation;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SnapshotDbHandlerImpl implements SnapshotDbHandler {

    private final ScenarioRepository scenarioRepository;
    private final HubRouterClient hubRouterClient;

    @Override
    @Transactional
    public void handler(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        Map<String, SensorStateAvro> states = snapshot.getSensorsState();
        List<Scenario> scenarios = scenarioRepository.findAllByHubId(hubId);

        for (Scenario scenario : scenarios) {
            if (matchesScenario(scenario, states)) {
                hubRouterClient.sendDeviceRequestAction(hubId, scenario.getName(), scenario.getActions());
            }
        }
    }

    private boolean matchesScenario(Scenario scenario, Map<String, SensorStateAvro> stateMap) {
        return scenario.getConditions().stream()
                .allMatch(sc -> checkCondition(sc, stateMap));
    }

    private boolean checkCondition(ScenarioCondition scenarioCondition, Map<String, SensorStateAvro> states) {
        String sensorId = scenarioCondition.getSensor().getId();
        SensorStateAvro sensorStateAvro = states.get(sensorId);

        if (sensorStateAvro == null) {
            return false;
        }

        return extractValueFromSensor(sensorStateAvro, scenarioCondition.getCondition().getType())
                .map(actualValue -> {
                    Condition condition = scenarioCondition.getCondition();
                    return hasOperation(condition.getOperation(), actualValue, condition.getValue());
                }).orElse(false);
    }

    private Optional<Integer> extractValueFromSensor(SensorStateAvro state, ConditionType type) {
        Object data = state.getData();

        return switch (data) {
            case ClimateSensorAvro climateSensorAvro -> switch (type) {
                case TEMPERATURE -> Optional.of(climateSensorAvro.getTemperatureC());
                case HUMIDITY -> Optional.of(climateSensorAvro.getHumidity());
                case CO2LEVEL -> Optional.of(climateSensorAvro.getCo2Level());
                default -> {
                    log.warn("Неизвестный показатель для климатического датчика.");
                    yield Optional.empty();
                }
            };
            case LightSensorAvro lightSensorAvro -> {
                if (type.equals(ConditionType.LUMINOSITY)) {
                    yield Optional.of(lightSensorAvro.getLuminosity());
                } else {
                    log.warn("Неизвестный показатель для датчика освещения.");
                    yield Optional.empty();
                }
            }
            case MotionSensorAvro motionSensorAvro -> {
                if (type.equals(ConditionType.MOTION)) {
                    yield Optional.of(motionSensorAvro.getMotion() ? 1 : 0);
                } else {
                    log.warn("Неизвестный показатель для датчика движения.");
                    yield Optional.empty();
                }
            }
            case SwitchSensorAvro switchSensorAvro -> {
                if (type.equals(ConditionType.SWITCH)) {
                    yield Optional.of(switchSensorAvro.getState() ? 1 : 0);
                } else {
                    log.warn("Неизвестный показатель для датчика переключения.");
                    yield Optional.empty();
                }
            }
            default -> {
                log.warn("Неизвестный тип датчика.");
                yield Optional.empty();
            }
        };
    }

    private boolean hasOperation(Operation operation, int actual, int target) {
        return switch (operation) {
            case EQUALS -> actual == target;
            case GREATER_THAN -> actual > target;
            case LOWER_THAN -> actual < target;
        };
    }
}
