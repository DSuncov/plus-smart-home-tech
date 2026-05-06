package ru.yandex.practicum.telemetry.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handlers.hub.BaseHubEventHandler;
import ru.yandex.practicum.telemetry.collector.service.handlers.sensor.BaseSensorEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
public class SmartHomeEventController extends CollectorControllerGrpc.CollectorControllerImplBase {

    private final Map<SensorEventProto.PayloadCase, BaseSensorEventHandler> sensorEventHandlers;
    private final Map<HubEventProto.PayloadCase, BaseHubEventHandler> hubEventHandlers;
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

    @Override
    public void collectSensorEvent(SensorEventProto sensorEventProto, StreamObserver<Empty> responseObserver) {
        BaseSensorEventHandler sensorEventHandler = sensorEventHandlers.get(sensorEventProto.getPayloadCase());
        try {
            if (sensorEventHandler == null) {
                throw new IllegalArgumentException("Обработчик для такого события отсутствует.");
            }

            kafkaEventProducer.send(sensorEventProto, sensorEventHandler);
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto hubEventProto, StreamObserver<Empty> responseObserver) {
        BaseHubEventHandler hubEventHandler = hubEventHandlers.get(hubEventProto.getPayloadCase());
        try {
            if (hubEventHandler == null) {
                throw new IllegalArgumentException("Обработчик для такого события отсутствует.");
            }

            kafkaEventProducer.send(hubEventProto, hubEventHandler);
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}
