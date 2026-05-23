package ru.yandex.practicum.telemetry.analyzer.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("grpc.client.hub-router")
public record GrpcProperties(
        String address,
        String port,
        Boolean enableKeepAlive,
        Boolean keepAliveWithoutCalls,
        String negotiationType
) {}
