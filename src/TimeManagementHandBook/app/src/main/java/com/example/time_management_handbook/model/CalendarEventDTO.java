package com.example.time_management_handbook.model;

import com.google.api.client.util.DateTime;

import java.time.Duration;

public class CalendarEventDTO {
    private String id;
    private String summary;
    private String description;
    private DateTime start;
    private DateTime end;
    private String recurrenceInfo;
    private String location;
    private Duration duration;
    private String creatorEmail;

    public CalendarEventDTO(String id, String summary, String description, DateTime start, DateTime end, String recurrenceInfo, String location, Duration duration, String creatorEmail) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.start = start;
        this.end = end;
        this.recurrenceInfo = recurrenceInfo;
        this.location = location;
        this.duration = duration;
        this.creatorEmail = creatorEmail;
    }

    public String getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getStart() {
        return start;
    }

    public DateTime getEnd() {
        return end;
    }

    public String getRecurrenceInfo() {
        return recurrenceInfo;
    }

    public String getLocation() {
        return location;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    public void setRecurrenceInfo(String recurrenceInfo) {
        this.recurrenceInfo = recurrenceInfo;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }
}

