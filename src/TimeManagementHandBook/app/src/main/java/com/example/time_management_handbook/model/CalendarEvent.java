package com.example.time_management_handbook.model;


import com.google.api.client.util.DateTime;

public class CalendarEvent {
    private String id;
    private String title;
    private String description;
    private DateTime startTime;
    private DateTime endTime;

    public CalendarEvent(String id, String title, String description, DateTime startTime, DateTime endTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
    public String getId() {return id;}
    public void setId() {this.id = id;}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }
}


