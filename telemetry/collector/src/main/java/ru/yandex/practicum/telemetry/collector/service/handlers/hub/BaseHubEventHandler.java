package ru.yandex.practicum.telemetry.collector.service.handlers.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.model.enums.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;

@Component
public interface BaseHubEventHandler {

    HubEventAvro mapToAvro(HubEvent event);

    HubEventType getType();
}
