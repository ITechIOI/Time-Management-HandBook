package com.example.time_management_handbook.model;

import com.google.api.client.util.DateTime;

import java.time.Duration;

public class Event_Of_The_Day_DTO {
    String idEvent;
    String idUser;
    String summary;
    String location;
    DateTime startTime;
    DateTime endTime;
    Duration notification_period;
    String description;
    int color;
    boolean id_deleted;


    public Event_Of_The_Day_DTO(String idEvent, String idUser, String summary, String location, DateTime startTime, DateTime endTime, Duration notification_period, String description, int color, boolean id_deleted) {
        this.idEvent = idEvent;
        this.idUser = idUser;
        this.summary = summary;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notification_period = notification_period;
        this.description = description;
        this.color = color;
        this.id_deleted = id_deleted;
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

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
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

    public boolean isId_deleted() {
        return id_deleted;
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

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(DateTime endTime) {
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

    public void setId_deleted(boolean id_deleted) {
        this.id_deleted = id_deleted;
    }
}
