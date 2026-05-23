package ru.yandex.practicum.telemetry.analyzer.client;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class HubRouterClient {

    @GrpcClient("hub-router")
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public void sendDeviceRequestAction(String hubId, String name, DeviceActionAvro action) {
        try {
            DeviceActionRequest request = convert(hubId, name, action);
            hubRouterClient.handleDeviceAction(request);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения по gRPC");
        }
    }

    private DeviceActionRequest convert(String hubId, String name, DeviceActionAvro action) {
        DeviceActionProto.Builder deviceActionProto = DeviceActionProto.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(ActionTypeProto.valueOf(action.getType().name()));

                if (action.getValue() != null) {
                    deviceActionProto.setValue(action.getValue());
                }

                deviceActionProto.build();

        Instant currentTime = Instant.now();

        return DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(name)
                .setAction(deviceActionProto)
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(currentTime.getEpochSecond())
                        .setNanos(currentTime.getNano())
                        .build())
                .build();
    }
}
