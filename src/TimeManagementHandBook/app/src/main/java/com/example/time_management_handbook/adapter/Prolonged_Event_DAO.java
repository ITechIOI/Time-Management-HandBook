package com.example.time_management_handbook.adapter;

import android.util.Log;

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
}

    /*String idEvent;
    String idUser;
    String summary;
    String location;
    Date startDate;
    Date endDate;
    Duration notification_period;
    String description;
    int color;
    boolean id_deleted;*/