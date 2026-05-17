package ru.yandex.practicum.telemetry.collector.utils.handlers.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Component
public class DeviceRemovedEventHandler implements BaseHubEventHandler {

    @Override
    public HubEventAvro mapToAvro(HubEventProto hubEvent) {
        DeviceRemovedEventAvro payload = DeviceRemovedEventAvro.newBuilder()
                .setId(hubEvent.getDeviceRemoved().getId())
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        hubEvent.getTimestamp().getSeconds(),
                        hubEvent.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }
}
