package com.example.time_management_handbook.model;

import com.google.api.client.util.DateTime;

import java.time.Duration;
import java.time.LocalDateTime;

public class Event_Of_The_Day_DTO {
    String idEvent;
    String idUser;
    String summary;
    String location;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Duration notification_period;
    String description;
    int color;


    public Event_Of_The_Day_DTO(String idEvent, String idUser, String summary, String location, LocalDateTime startTime, LocalDateTime endTime, Duration notification_period, String description, int color) {
        this.idEvent = idEvent;
        this.idUser = idUser;
        this.summary = summary;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notification_period = notification_period;
        this.description = description;
        this.color = color;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public String getUserId() {
        return idUser;
    }

    public String getSummary() {
        return summary;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Duration getNotification_period() {
        return notification_period;
    }

    public String getDescription() {
        return description;
    }

    public int getColor() {
        return color;
    }


    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public void setUserId(String userId) {
        this.idUser = userId;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setNotification_period(Duration notification_period) {
        this.notification_period = notification_period;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Event_Of_The_Day_DTO{" +
                "idEvent='" + idEvent + '\'' +
                ", idUser='" + idUser + '\'' +
                ", summary='" + summary + '\'' +
                ", location='" + location + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", notification_period=" + notification_period +
                ", description='" + description + '\'' +
                ", color=" + color +
                '}';
    }

    public String getIdUser() {
        return idUser;
    }
}
