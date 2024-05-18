package com.example.time_management_handbook.model;

import com.google.api.client.util.DateTime;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

public class Prolonged_Event_DTO implements Serializable {
    String idEvent;
    String idUser;
    String summary;
    String location;
    LocalDate startDate;
    LocalDate endDate;
    Duration notification_period;
    String description;
    int color;

    public Prolonged_Event_DTO(String idEvent, String userId, String summary, String location, LocalDate startDate, LocalDate endDate, Duration notification_period, String description, int color) {
        this.idEvent = idEvent;
        this.idUser = userId;
        this.summary = summary;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
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

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
        return "Prolonged_Event_DTO{" +
                "idEvent='" + idEvent + '\'' +
                ", idUser='" + idUser + '\'' +
                ", summary='" + summary + '\'' +
                ", location='" + location + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", notification_period=" + notification_period +
                ", description='" + description + '\'' +
                ", color=" + color +
                '}';
    }
}
