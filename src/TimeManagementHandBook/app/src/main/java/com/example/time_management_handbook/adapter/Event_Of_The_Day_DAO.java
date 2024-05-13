package com.example.time_management_handbook.adapter;

import android.util.Log;

import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.google.api.client.util.DateTime;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Event_Of_The_Day_DAO {

    public static Event_Of_The_Day_DAO instance;
    private Event_Of_The_Day_DAO() {}
    public static Event_Of_The_Day_DAO getInstance() {
        if (instance == null) {
            instance = new Event_Of_The_Day_DAO();
        }
        return instance;
    }
    public List<Event_Of_The_Day_DTO> getListEventOfTheDay(String email, LocalDateTime timeNow) {
        List<Event_Of_The_Day_DTO> listEvents = new ArrayList<>();

        LocalDate date = timeNow.toLocalDate();
        LocalTime time = timeNow.toLocalTime();
        String dateTime = date.toString() + " " + time.toString();

        String query = "EXEC USP_GET_EVENT_OF_THE_DAY_BY_ID '" + email + "', '" + dateTime + "'";

        try {
            ResultSet resultSet = DataProvider.getInstance().executeQuery(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    String idEvent = resultSet.getString(1);
                    String idUser = resultSet.getString(2);
                    String summary = resultSet.getString(3);
                    String location = resultSet.getString(4);

                    Timestamp startTime = resultSet.getTimestamp(5);
                    ZonedDateTime zonedDateTimeStart = startTime.toInstant().atZone(ZoneId.systemDefault());
                    LocalDateTime start = zonedDateTimeStart.toLocalDateTime();

                    Timestamp endTime = resultSet.getTimestamp(6);
                    ZonedDateTime zonedDateTimeEnd = endTime.toInstant().atZone(ZoneId.systemDefault());
                    LocalDateTime end = zonedDateTimeEnd.toLocalDateTime();

                    Duration notification_period = Duration.parse(resultSet.getString(7));
                    String description = resultSet.getString(8);
                    int color = resultSet.getInt(9);

                    Event_Of_The_Day_DTO event = new Event_Of_The_Day_DTO(idEvent, idUser, summary, location,
                            start, end, notification_period, description, color);
                    listEvents.add(event);
                    Log.d("Each event: ", event.toString());
                }
            }
        } catch (SQLException e) {
            Log.d("Get list event of the day: ", e.getMessage());
        };

        return listEvents;
    }

    public int InsertNewEvent(String email, Event_Of_The_Day_DTO event) {
        int count = 0;

        String summary = event.getSummary();
        String location = event.getLocation();
        LocalDateTime start = event.getStartTime();
        LocalDateTime end = event.getEndTime();
        Duration duration = event.getNotification_period();
        String description = event.getDescription();
        int color = event.getColor();

        LocalDateTime startRoundedDateTime = start.with(LocalTime.from(start.toLocalTime().withSecond(start.getSecond()).withNano(0)));
        LocalDate startDate = startRoundedDateTime.toLocalDate();
        LocalTime startTime = startRoundedDateTime.toLocalTime();
        String start_String = startDate.toString() + " " + startTime.toString();

        LocalDateTime endRoundedDateTime = end.with(LocalTime.from(end.toLocalTime().withSecond(end.getSecond()).withNano(0)));
        LocalDate endDate = endRoundedDateTime.toLocalDate();
        LocalTime endTime = endRoundedDateTime.toLocalTime();
        String end_String = endDate.toString() + " " + endTime.toString();
        Log.d("Time Locate: ", start_String + "       " + end_String);

        String query = "EXEC INSERT_NEW_EVENT_OF_THE_DAY '" + email + "','" +
                summary + "','" + location + "','" + start_String + "','" + end_String + "','" +
                duration + "','" + description + "'," + color ;
        try {
            count = DataProvider.getInstance().executeNonQuery(query);
            Log.d("Insert new event of the day: ", String.valueOf(count));
        }catch (Exception e) {
            Log.d("Insert new event of the day: ", e.getMessage());
        }

        return count;
    }

    public int deleteEventOfTheDay(String email, Event_Of_The_Day_DTO event) {
        int rowEffect = -1;

        String summary = event.getSummary();
        LocalDateTime start = event.getStartTime();
        LocalDateTime end = event.getEndTime();

        String start_String = start.toString().replace("T", " ");
        String end_String = end.toString().replace("T", " ");

        String query = "EXEC USP_DELETE_EVENT_OF_THE_DAY '" + email +
                "','" + summary + "','" + start_String + "','" + end_String + "'" ;

        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            //Log.d("Delete event of the day: ", String.valueOf(rowEffect));
            Log.d("Delete event of the day: ", String.valueOf(rowEffect));
        }catch (Exception e) {
            Log.d("Delete event of the day: ", e.getMessage());
        }

        return rowEffect;
    }

    public int UpdateEventOfTheDay(Event_Of_The_Day_DTO event) {
        int rowEffect = -1;

        String idEvent = event.getIdEvent();
        String summary = event.getSummary();
        String location = event.getLocation();
        LocalDateTime start = event.getStartTime();
        LocalDateTime end = event.getEndTime();
        Duration notification = event.getNotification_period();
        String description = event.getDescription();
        int color = event.getColor();

        LocalDateTime startRoundedDateTime = start.with(LocalTime.from(start.toLocalTime().withSecond(start.getSecond()).withNano(0)));
        String startTime = startRoundedDateTime.toString().replace("T", " ");
        LocalDateTime endRoundedDateTime = end.with(LocalTime.from(end.toLocalTime().withSecond(end.getSecond()).withNano(0)));
        String endTime = endRoundedDateTime.toString().replace("T", " ");

        String query = "EXEC USP_UPDATE_EVENT_OF_THE_DAY '" + idEvent + "','" +
                summary + "','" + location + "','" + startTime + "','" +
                endTime + "','" + notification.toString() + "','" +
                description +  "'," + color;

        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            //Log.d("Update event of the day: ", String.valueOf(rowEffect));
            Log.d("Update event of the day: ", query);
        }catch (Exception e) {
            Log.d("Update event of the day: ", e.getMessage());
        }

        return rowEffect;
    }
}

   /* String idEvent;
    String idUser;
    String summary;
    String location;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Duration notification_period;
    String description;
    int color;*/