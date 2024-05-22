package com.example.time_management_handbook.adapter;

import android.util.Log;

import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.Prolonged_Event_DTO;
import com.google.api.client.util.DateTime;

import java.sql.Date;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Prolonged_Event_DAO {

    public static Prolonged_Event_DAO instance;
    private Prolonged_Event_DAO() {}
    public static Prolonged_Event_DAO getInstance() {
        if (instance == null) {
            instance = new Prolonged_Event_DAO();
        }
        return instance;
    }
    public List<Prolonged_Event_DTO> getListProlongedEvent(String email, LocalDate timeNow) {
        List<Prolonged_Event_DTO> listEvents = new ArrayList<>();
        String query = "EXEC USP_GET_PROLONGED_EVENT_BY_EMAIL '" + email + "','" +
                timeNow + "'";

        try {
            ResultSet resultSet = DataProvider.getInstance().executeQuery(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    String idEvent = resultSet.getString(1);
                    String idUser = resultSet.getString(2);
                    String summary = resultSet.getString(3);
                    String location = resultSet.getString(4);

                    Date startDateSql = resultSet.getDate(5);
                    LocalDate startDate = startDateSql.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    Date endDateSql = resultSet.getDate(6);
                    LocalDate endDate = endDateSql.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    Duration notification_period = Duration.parse(resultSet.getString(7));
                    String description = resultSet.getString(8);
                    int color = resultSet.getInt(9);

                    Prolonged_Event_DTO event = new Prolonged_Event_DTO(idEvent, idUser, summary, location,
                            startDate, endDate, notification_period, description, color);

                    listEvents.add(event);
                    Log.d("Each prolonged event: ", event.toString());
                }
            }
        }catch (Exception e) {
            Log.d("Get prolonged event: ", e.getMessage());
        }

        return listEvents;
    }

    public List<Prolonged_Event_DTO> getListProlongedEventForNotification(String email, LocalDate timeNow) {
        List<Prolonged_Event_DTO> listEvents = new ArrayList<>();
        String query = "EXEC USP_GET_PROLONGED_EVENT_BY_EMAIL_FOR_NOTIFICATION '" + email + "','" +
                timeNow + "'";

        try {
            ResultSet resultSet = DataProvider.getInstance().executeQuery(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    String idEvent = resultSet.getString(1);
                    String idUser = resultSet.getString(2);
                    String summary = resultSet.getString(3);
                    String location = resultSet.getString(4);

                    Date startDateSql = resultSet.getDate(5);
                    LocalDate startDate = startDateSql.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    Date endDateSql = resultSet.getDate(6);
                    LocalDate endDate = endDateSql.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    Duration notification_period = Duration.parse(resultSet.getString(7));
                    String description = resultSet.getString(8);
                    int color = resultSet.getInt(9);

                    Prolonged_Event_DTO event = new Prolonged_Event_DTO(idEvent, idUser, summary, location,
                            startDate, endDate, notification_period, description, color);

                    listEvents.add(event);
                    Log.d("Each prolonged event: ", event.toString());
                }
            }
        }catch (Exception e) {
            Log.d("Get prolonged event: ", e.getMessage());
        }

        return listEvents;
    }

    public List<LocalDate> getListProlongedEventByDayOfMonth(String email, LocalDate timeNow) {
        List<LocalDate> listLocalDate = new ArrayList<>();
        String query = "EXEC USP_GET_PROLONGED_EVENT_BY_DAY_OF_MONTH '" + email + "','" +
                timeNow + "'";

        try {
            ResultSet resultSet = DataProvider.getInstance().executeQuery(query);
            if (resultSet != null) {
                while (resultSet.next()) {

                    Date startDateSql = resultSet.getDate(5);
                    LocalDate startDate = startDateSql.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    Date endDateSql = resultSet.getDate(6);
                    LocalDate endDate = endDateSql.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    List<LocalDate> localDateBetweenTwoLocalDate = generateDates(startDate, endDate);
                    for(int i = 0; i < localDateBetweenTwoLocalDate.size(); i++) {
                        if (timeNow.getMonth() == localDateBetweenTwoLocalDate.get(i).getMonth()) {
                            listLocalDate.add(localDateBetweenTwoLocalDate.get(i));
                        }
                    }

                }
            }
            Log.d("Get list local date that contains prolonged event: ", listLocalDate.toString());
        }catch (Exception e) {
            Log.d("Get prolonged event: ", e.getMessage());
        }

        return listLocalDate;
    }

    public static List<LocalDate> generateDates(LocalDate start, LocalDate end) {
        return IntStream.rangeClosed((int)start.toEpochDay(), (int) end.toEpochDay())
                .mapToObj(day -> LocalDate.ofEpochDay(day))
                .collect(Collectors.toList());
    }


    public int InsertNewProlongedEvent(String email, Prolonged_Event_DTO event) {
        int rowEffect = -1;

        String summary = event.getSummary();
        String location = event.getLocation();
        LocalDate start = event.getStartDate();
        LocalDate end = event.getEndDate();
        Duration duration = event.getNotification_period();
        String description = event.getDescription();
        int color = event.getColor();

        String query = "EXEC USP_INSERT_NEW_PROLONGED_OF_THE_DAY '" + email + "','" +
                summary + "','" + location + "','" + start + "','" + end + "','" +
                duration + "','" + description + "'," + color ;

        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            Log.d("Insert new prolonged event: ", String.valueOf(rowEffect));
        }catch (Exception e) {
            Log.d("Insert new prolonged event: ", e.getMessage());
        }

        return rowEffect;
    }

    public int InsertNewProlongedEventFromCalendar(String email, Prolonged_Event_DTO event) {
        int rowEffect = -1;

        String summary = event.getSummary();
        String location = event.getLocation();
        LocalDate start = event.getStartDate();
        LocalDate end = event.getEndDate();
        Duration duration = event.getNotification_period();
        String description = event.getDescription();
        int color = event.getColor();

        String query = "EXEC USP_INSERT_PROLONGED_EVENT_FROM_CALENDAR '" + email + "','" +
                summary + "','" + location + "','" + start + "','" + end + "','" +
                duration + "','" + description + "'," + color ;

        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            Log.d("Insert new prolonged event: ", String.valueOf(rowEffect));
        }catch (Exception e) {
            Log.d("Insert new prolonged event: ", e.getMessage());
        }

        return rowEffect;
    }

    public int deleteProlongedEvent(String email, Prolonged_Event_DTO event) {
        int rowEffect = -1;

        String summary = event.getSummary();
        LocalDate start = event.getStartDate();
        LocalDate end = event.getEndDate();

        String query = "EXEC USP_DELETE_PROLONGED_EVENT '" + email +
                "','" + summary + "','" + start + "','" + end + "'" ;

        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            // Log.d("Delete event of the day: ", query);
            Log.d("Delete prolonged event: ", String.valueOf(rowEffect));
        }catch (Exception e) {
            Log.d("Delete prolonged event: ", e.getMessage());
        }

        return rowEffect;
    }

    public int UpdateProlongedEvent(Prolonged_Event_DTO  event) {
        int rowEffect = -1 ;

        String idEvent = event.getIdEvent();
        String summary = event.getSummary();
        String location = event.getLocation();
        LocalDate start = event.getStartDate();
        LocalDate end = event.getEndDate();
        Duration notification = event.getNotification_period();
        String description = event.getDescription();
        int color = event.getColor();

        String query = "EXEC USP_UPDATE_PROLONGED_EVENT '" + idEvent + "','" +
                summary + "','" + location + "','" + start + "','" +
                end + "','" + notification.toString() + "','" +
                description +  "'," + color;

        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            Log.d("Update event of the day: ", String.valueOf(rowEffect));
            //Log.d("Update event of the day: ", query);
        }catch (Exception e) {
            Log.d("Update event of the day: ", e.getMessage());
        }

        return rowEffect;
    }



}
