package ru.yandex.practicum.telemetry.collector.service.handlers.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Component
public class DeviceAddedEventHandler implements BaseHubEventHandler {

    @Override
    public HubEventAvro mapToAvro(HubEventProto hubEvent) {
        DeviceAddedEventAvro payload = DeviceAddedEventAvro.newBuilder()
                .setId(hubEvent.getDeviceAdded().getId())
                .setType(selectionType(hubEvent))
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
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    private DeviceTypeAvro selectionType(HubEventProto hubEventProto) {
        return switch (hubEventProto.getDeviceAdded().getType()) {
            case DeviceTypeProto.CLIMATE_SENSOR -> DeviceTypeAvro.CLIMATE_SENSOR;
            case DeviceTypeProto.MOTION_SENSOR -> DeviceTypeAvro.MOTION_SENSOR;
            case DeviceTypeProto.LIGHT_SENSOR -> DeviceTypeAvro.LIGHT_SENSOR;
            case DeviceTypeProto.SWITCH_SENSOR -> DeviceTypeAvro.SWITCH_SENSOR;
            case DeviceTypeProto.TEMPERATURE_SENSOR -> DeviceTypeAvro.TEMPERATURE_SENSOR;
            default -> null;
        };
    }
}
