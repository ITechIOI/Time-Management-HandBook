package com.example.time_management_handbook.model;

import com.google.api.client.util.DateTime;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.security.auth.Subject;

public class TaskDTO {
    String idTask;
    String idUser;
    String name;
    String location;
    LocalDateTime creatingTime;
    LocalDateTime endTime;
    Duration notification_period;
    String description;
    LocalDateTime finishedTime;
    int color;

    public TaskDTO(String idTask, String idUser, String name, String location, LocalDateTime creatingTime, LocalDateTime endTime, Duration notification_period, String description, LocalDateTime finishedTime, int color) {
        this.idTask = idTask;
        this.idUser = idUser;
        this.name = name;
        this.location = location;
        this.creatingTime = creatingTime;
        this.endTime = endTime;
        this.notification_period = notification_period;
        this.description = description;
        this.finishedTime = finishedTime;
        this.color = color;
    }


    public String getIdTask() {
        return idTask;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatingTime() {
        return creatingTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getFinishedTime() {
        return finishedTime;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatingTime(LocalDateTime creatingTime) {
        this.creatingTime = creatingTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFinishedTime(LocalDateTime finishedTime) {
        this.finishedTime = finishedTime;
    }

    public Duration getNotification_period() {
        return notification_period;
    }

    public void setNotification_period(Duration notification_period) {
        this.notification_period = notification_period;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "idTask='" + idTask + '\'' +
                ", idUser='" + idUser + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", creatingTime=" + creatingTime +
                ", endTime=" + endTime +
                ", notification_period=" + notification_period +
                ", description='" + description + '\'' +
                ", finishedTime=" + finishedTime +
                ", color=" + color +
                '}';
    }
}
