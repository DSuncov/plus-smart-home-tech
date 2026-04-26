package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.model.enums.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.enums.SensorEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.SensorEvent;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handlers.hub.BaseHubEventHandler;
import ru.yandex.practicum.telemetry.collector.service.handlers.sensor.BaseSensorEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/events")
public class SmartHomeEventController {

    private final Map<SensorEventType, BaseSensorEventHandler> sensorEventHandlers;
    private final Map<HubEventType, BaseHubEventHandler> hubEventHandlers;
    private final KafkaEventProducer kafkaEventProducer;

    public SmartHomeEventController(
            Set<BaseSensorEventHandler> sensorEventHandlers,
            Set<BaseHubEventHandler> hubEventHandlers, KafkaEventProducer kafkaEventProducer) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(BaseSensorEventHandler::getType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(BaseHubEventHandler::getType, Function.identity()));
        this.kafkaEventProducer = kafkaEventProducer;
    }

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @NotNull @RequestBody SensorEvent event) {
        BaseSensorEventHandler sensorEventHandler = sensorEventHandlers.get(event.getType());
        if (sensorEventHandler == null) {
            throw new IllegalArgumentException("Обработчик для такого события отсутствует.");
        }
        kafkaEventProducer.send(event, sensorEventHandler);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @NotNull @RequestBody HubEvent event) {
        BaseHubEventHandler hubEventHandler = hubEventHandlers.get(event.getType());
        if (hubEventHandler == null) {
            throw new IllegalArgumentException("Обработчик для такого события отсутствует.");
        }
        kafkaEventProducer.send(event, hubEventHandler);
    }
}
