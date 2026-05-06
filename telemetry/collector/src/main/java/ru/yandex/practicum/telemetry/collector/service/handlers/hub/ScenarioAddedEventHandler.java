package ru.yandex.practicum.telemetry.collector.service.handlers.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;

@Component
public class ScenarioAddedEventHandler implements BaseHubEventHandler {

    @Override
    public HubEventAvro mapToAvro(HubEventProto hubEvent) {
        ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                .setName(hubEvent.getScenarioAdded().getName())
                .setConditions(hubEvent.getScenarioAdded().getConditionList().stream()
                        .map(s -> ScenarioConditionAvro.newBuilder()
                                    .setSensorId(s.getSensorId())
                                    .setType(selectConditionType(s))
                                    .setOperation(selectConditionOperation(s))
                                    .setValue(s.getValueCase())
                                    .build())
                        .toList())
                .setActions(hubEvent.getScenarioAdded().getActionList().stream()
                        .map(s -> DeviceActionAvro.newBuilder()
                                .setSensorId(s.getSensorId())
                                .setType(selectActionType(s))
                                .setValue(s.getValue())
                                .build())
                        .toList())
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        hubEvent.getTimestamp().getSeconds(),
                        hubEvent.getTimestamp().getNanos()
                ))
                .setPayload(payload)
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    private ConditionTypeAvro selectConditionType(ScenarioConditionProto scenarioConditionProto) {
        return switch (scenarioConditionProto.getType()) {
            case ConditionTypeProto.MOTION -> ConditionTypeAvro.MOTION;
            case ConditionTypeProto.LUMINOSITY -> ConditionTypeAvro.LUMINOSITY;
            case ConditionTypeProto.SWITCH -> ConditionTypeAvro.SWITCH;
            case ConditionTypeProto.TEMPERATURE -> ConditionTypeAvro.TEMPERATURE;
            case ConditionTypeProto.CO2LEVEL -> ConditionTypeAvro.CO2LEVEL;
            case ConditionTypeProto.HUMIDITY -> ConditionTypeAvro.HUMIDITY;
            default -> null;
        };
    }

    private ConditionOperationAvro selectConditionOperation(ScenarioConditionProto scenarioConditionProto) {
        return switch (scenarioConditionProto.getOperation()) {
            case ConditionOperationProto.EQUALS -> ConditionOperationAvro.EQUALS;
            case ConditionOperationProto.GREATER_THAN -> ConditionOperationAvro.GREATER_THAN;
            case ConditionOperationProto.LOWER_THAN -> ConditionOperationAvro.LOWER_THAN;
            default -> null;
        };
    }

    private ActionTypeAvro selectActionType(DeviceActionProto deviceActionProto) {
        return switch (deviceActionProto.getType()) {
            case ActionTypeProto.ACTIVATE -> ActionTypeAvro.ACTIVATE;
            case ActionTypeProto.DEACTIVATE -> ActionTypeAvro.DEACTIVATE;
            case ActionTypeProto.INVERSE -> ActionTypeAvro.INVERSE;
            case ActionTypeProto.SET_VALUE -> ActionTypeAvro.SET_VALUE;
            default -> null;
        };
    }
}
