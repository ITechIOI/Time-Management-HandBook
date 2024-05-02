package com.example.time_management_handbook.model;

import com.google.api.client.util.DateTime;

public class CalendarEvent {
    private String id;
    private String summary;
    private String description;
    private DateTime start;
    private DateTime end;
    private String recurrenceInfo;
    private String location; // Địa điểm
    private String creatorEmail; // Email của người tạo sự kiện

    public CalendarEvent(String id, String summary, String description, DateTime start, DateTime end, String recurrenceInfo, String location, String creatorEmail) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.start = start;
        this.end = end;
        this.recurrenceInfo = recurrenceInfo;
        this.location = location;
        this.creatorEmail = creatorEmail;
    }
}

