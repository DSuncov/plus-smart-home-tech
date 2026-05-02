package ru.yandex.practicum.telemetry.collector.model.sensors;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.telemetry.collector.model.enums.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MotionSensorEvent extends SensorEvent {

    @NotNull(message = "Уровень качества связи должен быть указан.")
    Integer linkQuality;
    @NotNull(message = "Наличие/отсутствие движения должно быть указано.")
    Boolean motion;
    @NotNull(message = "Уровень напряжения должно быть указано.")
    Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
