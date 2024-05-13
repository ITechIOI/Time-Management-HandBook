package com.example.time_management_handbook.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.activity.AboutZEIT_Activity;
import com.example.time_management_handbook.activity.Home_Activity;
import com.example.time_management_handbook.activity.Task_Activity;
import com.example.time_management_handbook.activity.Task_Fragment;
import com.example.time_management_handbook.model.TaskDTO;
import com.google.api.client.util.DateTime;

import java.io.Serializable;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TaskDAO extends RecyclerView.Adapter<TaskDAO.TaskViewHolder>{


    public static TaskDAO instance;
    private List<TaskDTO> list;
    private Context tContext;
    public TaskDAO(List<TaskDTO> task, Context context)
    {
        this.list=task;
        this.tContext = context;
    }
    public TaskDAO() {}

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskDTO task = list.get(position);
        holder.taskTextview.setText(task.getName()+"\n"+task.getEndTime().toString());
        if (task.getFinishedTime()!=null)
            holder.taskCheckbox.setChecked(true);
        else holder.taskCheckbox.setChecked(false);
        switch (task.getColor())
        {
            case 1:
                holder.itemLayout.setBackgroundColor(Color.parseColor("#CEEDC7"));
                break;
            case 2:
                holder.itemLayout.setBackgroundColor(Color.parseColor("#FF9494"));
                break;
            case 3:
                holder.itemLayout.setBackgroundColor(Color.parseColor("#FFC8DD"));
                break;
            case 4:
                holder.itemLayout.setBackgroundColor(Color.parseColor("#D7E3FC"));
                break;
            case 5:
                holder.itemLayout.setBackgroundColor(Color.parseColor("#FFF6BD"));
                break;
            case 6:
                holder.itemLayout.setBackgroundColor(Color.parseColor("#FFD4B2"));
                break;

        }
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mit= new Intent(tContext, Task_Activity.class);
                //mit.putExtra("mytast", (Serializable) task);
                tContext.startActivity(mit);
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilterList(List<TaskDTO> filterTask) {
        this.list = filterTask;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout itemLayout;
        TextView taskTextview;
        CheckBox taskCheckbox;
        public TaskViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.item);
            taskTextview = itemView.findViewById(R.id.task);
            taskCheckbox = itemView.findViewById(R.id.an);
        }
    }

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

    // Tìm kiếm Task thông qua tên: không phân biệt hoa thường
    public List<TaskDTO> getListTaskByName(String email, String nameTask, LocalDateTime timeNow) {
        List<TaskDTO> listTask = new ArrayList<>();

        List<TaskDTO> listTasks = new ArrayList<>();
        LocalDate date = timeNow.toLocalDate();
        LocalTime time = timeNow.toLocalTime();
        String dateTimeNow = date.toString() + " " + time.toString();

        String query = "EXEC GET_LIST_TASK_BY_NAME '" + email + "', N'" + nameTask + "','" + dateTimeNow + "'";
        Log.d("Get list task by name: ", query);
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
                    listTask.add(task);

                    Log.d("Each task: ", task.toString());

                }
            }
        } catch ( Exception e) {
            Log.d("Get list task: ", e.getMessage());
        }

        return listTask;
    }

    public int InsertNewTask (String email, TaskDTO event) {
        int rowEffect = -1;

        String name = event.getName();
        String location = event.getLocation();
        LocalDateTime start = event.getCreatingTime();
        LocalDateTime end = event.getEndTime();
        Duration duration = event.getNotification_period();
        String description = event.getDescription();
        int color = event.getColor();
        LocalDateTime finish = event.getFinishedTime();

        LocalDateTime startRoundedDateTime = start.with(LocalTime.from(start.toLocalTime().withSecond(start.getSecond()).withNano(0)));
        LocalDate startDate = startRoundedDateTime.toLocalDate();
        LocalTime startTime = startRoundedDateTime.toLocalTime();
        String start_String = startDate.toString() + " " + startTime.toString();

        LocalDateTime endRoundedDateTime = end.with(LocalTime.from(end.toLocalTime().withSecond(end.getSecond()).withNano(0)));
        LocalDate endDate = endRoundedDateTime.toLocalDate();
        LocalTime endTime = endRoundedDateTime.toLocalTime();
        String end_String = endDate.toString() + " " + endTime.toString();

        LocalDateTime finishRoundedDateTime = finish.with(LocalTime.from(finish.toLocalTime().withSecond(finish.getSecond()).withNano(0)));
        LocalDate finishDate = finishRoundedDateTime.toLocalDate();
        LocalTime finishTime = finishRoundedDateTime.toLocalTime();
        String finish_String = finishDate.toString() + " " + finishTime.toString();

        String query = "EXEC USP_INSERT_NEW_TASK '" + email + "','" +
                name + "','" + location + "','" + start_String + "','" + end_String + "','" +
                duration + "','" + description + "','" + finish_String + "'," + color;
        Log.d("String query: ", query);

        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            Log.d("Insert new task: ", String.valueOf(rowEffect));
        }catch (Exception e) {
            Log.d("Insert new task: ", e.getMessage());
        }

        return rowEffect;
    }

    public int DeleteTask(String email, TaskDTO task) {
        int rowEffect = -1;

        String name = task.getName();
        LocalDateTime endTime = task.getEndTime();

        String query = "EXEC USP_DELETE_TASK '" + email + "','" +
                name + "','" + endTime + "'";

        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            // Log.d("Delete event of the day: ", query);
            Log.d("Delete task: ", String.valueOf(rowEffect));
        }catch (Exception e) {
            Log.d("Delete task: ", e.getMessage());
        }

        return rowEffect;
    }

    public int UpdateTask(TaskDTO task) {
        int rowEffect = -1;

        String idTask = task.getIdTask();
        String name = task.getName();
        String location = task.getLocation();
        LocalDateTime creating = task.getCreatingTime();
        LocalDateTime end = task.getEndTime();
        Duration notification = task.getNotification_period();
        String description = task.getDescription();
        LocalDateTime finish = task.getFinishedTime();
        int color = task.getColor();

        LocalDateTime creatingRoundedDateTime = creating.with(LocalTime.from(creating.toLocalTime().withSecond(creating.getSecond()).withNano(0)));
        String startTime = creatingRoundedDateTime.toString().replace("T", " ");
        LocalDateTime endRoundedDateTime = end.with(LocalTime.from(end.toLocalTime().withSecond(end.getSecond()).withNano(0)));
        String endTime = endRoundedDateTime.toString().replace("T", " ");
        LocalDateTime finishRoundedDateTime = finish.with(LocalTime.from(finish.toLocalTime().withSecond(finish.getSecond()).withNano(0)));
        String finishTime = finishRoundedDateTime.toString().replace("T", " ");

        String query = "EXEC USP_UPDATE_TASK '" + idTask + "','" + name + "','" +
                location + "','" + startTime + "','" + endTime + "','" + notification + "','" +
                description + "','" + finishTime + "'," + color;

        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            Log.d("Update task: ", String.valueOf(rowEffect));
            //Log.d("Update event of the day: ", query);
        }catch (Exception e) {
            Log.d("Update task: ", e.getMessage());
        }

        return rowEffect;
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