package ru.yandex.practicum.telemetry.collector.service.handlers.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.model.enums.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioAddedEvent;

@Component
public class ScenarioAddedEventHandler implements BaseHubEventHandler {

    @Override
    public HubEventAvro mapToAvro(HubEvent hubEvent) {
        ScenarioAddedEvent event = (ScenarioAddedEvent) hubEvent;
        ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setConditions(event.getConditions().stream()
                        .map(s -> ScenarioConditionAvro.newBuilder()
                                    .setSensorId(s.getSensorId())
                                    .setType(ConditionTypeAvro.valueOf(s.getType()))
                                    .setOperation(ConditionOperationAvro.valueOf(s.getOperation()))
                                    .setValue(s.getValue())
                                    .build())
                        .toList())
                .setActions(event.getActions().stream()
                        .map(s -> DeviceActionAvro.newBuilder()
                                .setSensorId(s.getSensorId())
                                .setType(ActionTypeAvro.valueOf(s.getType()))
                                .setValue(s.getValue())
                                .build())
                        .toList())
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
