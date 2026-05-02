package ru.yandex.practicum.telemetry.collector.model.hub;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.telemetry.collector.model.DeviceAction;
import ru.yandex.practicum.telemetry.collector.model.ScenarioCondition;
import ru.yandex.practicum.telemetry.collector.model.enums.HubEventType;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioAddedEvent extends HubEvent {

    @Size(min = 3, message = "Название добавленного сценария должно содержать не менее 3 символов.")
    String name;
    @NotEmpty(message = "Список условий, связанных со сценарием, не может быть пустым.")
    List<ScenarioCondition> conditions;
    @NotEmpty(message = "Список действия, которые должны быть выполнены, не может быть пустым.")
    List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
