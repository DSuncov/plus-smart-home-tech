package ru.yandex.practicum.telemetry.collector.service.handlers.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
public interface BaseHubEventHandler {

    HubEventAvro mapToAvro(HubEventProto event);

    HubEventProto.PayloadCase getType();
}
