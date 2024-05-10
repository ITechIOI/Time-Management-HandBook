package com.example.time_management_handbook.adapter;

import android.util.Log;

import com.example.time_management_handbook.model.TaskDTO;
import com.google.api.client.util.DateTime;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public static TaskDAO instance;
    private TaskDAO() {}
    public static TaskDAO getInstance() {
        if (instance == null) {
            instance = new TaskDAO();
        }
        return instance;
    }
    public List<TaskDTO> getListTask(String email, LocalDateTime timeNow) {
        List<TaskDTO> listTasks = new ArrayList<>();

        LocalDate date = timeNow.toLocalDate();
        LocalTime time = timeNow.toLocalTime();
        String dateTimeNow = date.toString() + " " + time.toString();

        String query = "EXEC USP_GET_TASK_BY_EMAIL '" + email + "','" + dateTimeNow + "'";

        try {
            ResultSet resultSet = DataProvider.getInstance().executeQuery(query);
            if (resultSet != null) {
                while (resultSet.next()) {

                    String idTask = resultSet.getString(1);
                    String idUser = resultSet.getString(2);
                    String name = resultSet.getString(3);
                    String location = resultSet.getString(4);

                    Timestamp creatingTime = resultSet.getTimestamp(5);
                    ZonedDateTime zonedDateTimeStart = creatingTime.toInstant().atZone(ZoneId.systemDefault());
                    LocalDateTime creating = zonedDateTimeStart.toLocalDateTime();

                    Timestamp endTime = resultSet.getTimestamp(6);
                    ZonedDateTime zonedDateTimeEnd = endTime.toInstant().atZone(ZoneId.systemDefault());
                    LocalDateTime end = zonedDateTimeEnd.toLocalDateTime();

                    Duration notification_period = Duration.parse(resultSet.getString(7));
                    String description = resultSet.getString(8);

                    Timestamp finishTime = resultSet.getTimestamp(5);
                    ZonedDateTime zonedDateTimeFinish = finishTime.toInstant().atZone(ZoneId.systemDefault());
                    LocalDateTime finish = zonedDateTimeFinish.toLocalDateTime();

                    int color = resultSet.getInt(10);

                    TaskDTO task = new TaskDTO(idTask, idUser, name, location, creating, end,
                            notification_period, description, finish, color);
                    listTasks.add(task);

                    Log.d("Each task: ", task.toString());

                }
            }
        } catch ( Exception e) {
            Log.d("Get list task: ", e.getMessage());
        }

        return  listTasks;
    }
}

   /* String idTask;
    String idUser;
    String name;
    String location;
    LocalDateTime creatingTime;
    LocalDateTime endTime;
    Duration notification_period;
    String description;
    DateTime finishedTime;*/