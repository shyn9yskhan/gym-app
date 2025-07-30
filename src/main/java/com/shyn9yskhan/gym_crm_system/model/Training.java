package com.shyn9yskhan.gym_crm_system.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Training {
    private String id;
    private String trainerId;
    private String name;
    private TrainingType type;
    private LocalDateTime date;
    private Duration duration;

    public Training() {}

    public Training(String id, String trainerId, String name, TrainingType type, LocalDateTime date, Duration duration) {
        this.id = id;
        this.trainerId = trainerId;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrainingType getType() {
        return type;
    }

    public void setType(TrainingType type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
