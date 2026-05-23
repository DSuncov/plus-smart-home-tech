package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scenarios")
@Builder
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "hub_id", nullable = false)
    @NotNull
    String hubId;

    @Column(name = "name", nullable = false)
    @NotBlank
    String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyColumn(table = "scenario_conditions", name = "sensor_id")
    @JoinTable(
            name = "scenario_conditions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id")
    )
    private Map<String, Condition> conditions;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyColumn(table = "scenario_actions", name = "sensor_id")
    @JoinTable(
            name = "scenario_actions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id")
    )
    private Map<String, Action> actions;
}
