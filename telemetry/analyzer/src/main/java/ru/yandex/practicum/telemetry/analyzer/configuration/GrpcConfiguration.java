package ru.yandex.practicum.telemetry.analyzer.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

@Configuration
@EnableConfigurationProperties(GrpcProperties.class)
@RequiredArgsConstructor
@Getter
public class GrpcConfiguration {
    private final GrpcProperties properties;

    @Bean
    public HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterStub() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 59090)
                .usePlaintext()
                .build();
        return HubRouterControllerGrpc.newBlockingStub(channel);
    }
}
