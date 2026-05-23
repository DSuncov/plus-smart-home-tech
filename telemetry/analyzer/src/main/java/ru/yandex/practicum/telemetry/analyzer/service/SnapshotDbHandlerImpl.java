package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.client.HubRouterClient;
import ru.yandex.practicum.telemetry.analyzer.model.*;
import ru.yandex.practicum.telemetry.analyzer.repository.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
@Slf4j
public class SnapshotDbHandlerImpl implements SnapshotDbHandler {

    private final ScenarioRepository scenarioRepository;
    private final HubRouterClient hubRouterClient;
    private final ScenarioConditionRepository scenarioConditionRepository;
    private final ScenarioActionRepository scenarioActionRepository;

    @Override
    @Transactional
    public void typeHandler(SensorsSnapshotAvro snapshot) {
        Set<Scenario> scenarios = scenarioRepository.findByHubIdWithConditions(snapshot.getHubId());
        scenarioRepository.findByHubIdWithActions(snapshot.getHubId());

        for (Scenario scenario : scenarios) {
            List<DeviceActionAvro> actions = evaluate(snapshot, scenario);
            for (DeviceActionAvro action : actions) {
                hubRouterClient.sendDeviceRequestAction(snapshot.getHubId(), scenario.getName(), action);
            }
        }
    }

    private List<DeviceActionAvro> evaluate(SensorsSnapshotAvro snapshot, Scenario scenario) {
        Map<String, SensorStateAvro> states = snapshot.getSensorsState();

        if (states == null || scenario.getConditions() == null) {
            return Collections.emptyList();
        }

        Set<ScenarioCondition> scenarioConditions = scenarioConditionRepository.findByScenarioId(scenario.getId());

        boolean allMatch = scenarioConditions.stream()
                .allMatch(sc -> sc.getSensor() != null && states.containsKey(sc.getSensor().getId()) &&
                        checkCondition(sc, states.get(sc.getSensor().getId())));

        if (!allMatch) {
            return Collections.emptyList();
        }

        Set<ScenarioAction> scenarioActions = scenarioActionRepository.findByScenarioId(scenario.getId());

        if (scenarioActions == null) {
            return Collections.emptyList();
        }

        return scenarioActions.stream()
                .map(sa -> {
                    DeviceActionAvro.Builder deviceActionAvro = DeviceActionAvro.newBuilder()
                            .setSensorId(sa.getSensor().getId())
                            .setType(ActionTypeAvro.valueOf(sa.getAction().getType().name()));

                    if (sa.getAction().getValue() != null) {
                        deviceActionAvro.setValue(sa.getAction().getValue());
                    }

                    return deviceActionAvro.build();
                })
                .toList();
    }

    private boolean checkCondition(ScenarioCondition scenarioCondition, SensorStateAvro state) {
        Condition condition = scenarioCondition.getCondition();

        if (condition == null || condition.getValue() == null || state == null || state.getData() == null) {
            return false;
        }

        Object payload = state.getData();

        Predicate<Integer> operation = switch (condition.getOperation()) {
            case EQUALS -> value -> value.equals(condition.getValue());
            case GREATER_THAN -> value -> value < condition.getValue();
            case LOWER_THAN -> value -> value > condition.getValue();
        };

        return switch (condition.getType()) {
            case MOTION -> payload instanceof MotionSensorAvro motionSensor && (motionSensor.getMotion() == (condition.getValue() == 1));
            case LUMINOSITY -> payload instanceof LightSensorAvro lightSensor && operation.test(lightSensor.getLuminosity()) ;
            case SWITCH -> payload instanceof SwitchSensorAvro switchSensor && (switchSensor.getState() == (condition.getValue() == 1));
            case TEMPERATURE -> payload instanceof ClimateSensorAvro climateSensor && operation.test(climateSensor.getTemperatureC());
            case CO2LEVEL -> payload instanceof ClimateSensorAvro climateSensor && operation.test(climateSensor.getCo2Level());
            case HUMIDITY -> payload instanceof ClimateSensorAvro climateSensor && operation.test(climateSensor.getHumidity());
        };
    }
}
