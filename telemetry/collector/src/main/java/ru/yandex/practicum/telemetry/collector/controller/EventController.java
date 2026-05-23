package ru.yandex.practicum.telemetry.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.EventProducer;
import ru.yandex.practicum.telemetry.collector.handlers.hub.BaseHubEventHandler;
import ru.yandex.practicum.telemetry.collector.handlers.sensor.BaseSensorEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {

    private final Map<SensorEventProto.PayloadCase, BaseSensorEventHandler> sensorEventHandlers;
    private final Map<HubEventProto.PayloadCase, BaseHubEventHandler> hubEventHandlers;
    private final EventProducer eventProducer;

    public EventController(
            Set<BaseSensorEventHandler> sensorEventHandlers,
            Set<BaseHubEventHandler> hubEventHandlers,
            EventProducer kafkaEventProducer) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(BaseSensorEventHandler::getType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(BaseHubEventHandler::getType, Function.identity()));
        this.eventProducer = kafkaEventProducer;
    }

    @Override
    public void collectSensorEvent(SensorEventProto sensorEventProto, StreamObserver<Empty> responseObserver) {
        BaseSensorEventHandler sensorEventHandler = sensorEventHandlers.get(sensorEventProto.getPayloadCase());
        try {
            if (sensorEventHandler == null) {
                throw new IllegalArgumentException("Обработчик для такого события отсутствует.");
            }
            eventProducer.send(sensorEventProto, sensorEventHandler);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
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
            eventProducer.send(hubEventProto, hubEventHandler);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}
