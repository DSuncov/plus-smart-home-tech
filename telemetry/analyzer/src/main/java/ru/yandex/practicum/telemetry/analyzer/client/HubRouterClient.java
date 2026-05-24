package ru.yandex.practicum.telemetry.analyzer.client;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioAction;

import java.time.Instant;
import java.util.List;

@Component
@Slf4j
public class HubRouterClient {

    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public HubRouterClient(@GrpcClient("hub-router") HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient) {
        this.hubRouterClient = hubRouterClient;
    }

    public void sendDeviceRequestAction(String hubId, String name, List<ScenarioAction> actions) {
        for (ScenarioAction scenarioAction : actions) {
            try {
                DeviceActionRequest request = convertForRequest(hubId, name, scenarioAction);
                hubRouterClient.handleDeviceAction(request);
            } catch (Exception e) {
                log.error("Ошибка при отправке сообщения по gRPC");
            }
        }
    }

    private DeviceActionRequest convertForRequest(String hubId, String name, ScenarioAction scenarioAction) {
        Action action = scenarioAction.getAction();
        DeviceActionProto.Builder deviceActionProto = DeviceActionProto.newBuilder()
                .setSensorId(scenarioAction.getSensor().getId())
                .setType(ActionTypeProto.valueOf(action.getType().name()));

                if (action.getType() != null) {
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
