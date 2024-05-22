package com.example.time_management_handbook.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.activity.AddTask_Activity;
import com.example.time_management_handbook.activity.Home_Activity;
import com.example.time_management_handbook.activity.Task_Activity;
import com.example.time_management_handbook.activity.Task_Fragment;
import com.example.time_management_handbook.model.TaskDTO;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskDAO extends RecyclerView.Adapter<TaskDAO.TaskViewHolder> {
    public static TaskDAO instance;
    private List<TaskDTO> list;
    private Context tContext;
    private Task_Fragment taskFragment;
    public TaskDAO(List<TaskDTO> task, Context context)
    {
        this.list=task;
        this.tContext = context;

    }
    public TaskDAO() {}

    public TaskDAO(List<TaskDTO> list, Context tContext, Task_Fragment taskFragment) {
        this.list = list;
        this.tContext = tContext;
        this.taskFragment = taskFragment;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskDTO task = list.get(position);

        Log.d("List task in task fragment: ", task.toString());

        LocalDateTime today = LocalDateTime.now();
        Duration duration = Duration.between(today, task.getEndTime());
        String remainingTime  = "";
        if (duration.toDays() == 0) {
            remainingTime = String.valueOf(duration.toHours() % 24) + "h " + String.valueOf(duration.toMinutes() % 60) +
                    "m " + String.valueOf(duration.getSeconds() % 60) + "s";
        } else {
            remainingTime = String.valueOf(duration.toDays()) + "d " +
                String.valueOf(duration.toHours() % 24) + "h " + String.valueOf(duration.toMinutes() % 60) +
                "m " + String.valueOf(duration.getSeconds() % 60) + "s";
        }

        int year = task.getEndTime().getYear();
        int month = task.getEndTime().getMonthValue();
        int dayOfMonth = task.getEndTime().getDayOfMonth();

         String deadlineString = "";
        if ((month > 0 && month < 10) && (dayOfMonth > 0 && dayOfMonth < 10)) {
            deadlineString = "0" + String.valueOf(dayOfMonth) + "/0" + String.valueOf(month) + "/" +
                    String.valueOf(year) + " " + task.getEndTime().toLocalTime().toString();
        } else if (month > 0 && month < 10) {
            deadlineString = String.valueOf(dayOfMonth) + "/0" + String.valueOf(month) + "/" +
                    String.valueOf(year) + " " + task.getEndTime().toLocalTime().toString();
        } else if (dayOfMonth > 0 && dayOfMonth < 10) {
            deadlineString = "0" + String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" +
                    String.valueOf(year) + " " + task.getEndTime().toLocalTime().toString();
        } else {
            deadlineString = String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" +
                    String.valueOf(year) + " " + task.getEndTime().toLocalTime().toString();
        }

        holder.nameTask.setText(task.getName());
        holder.deadlineTask.setText(deadlineString);
        holder.timeleftTask.setText(remainingTime);
        if (task.getFinishedTime()!=null)
            holder.taskCheckbox.setChecked(true);
        else holder.taskCheckbox.setChecked(false);
        Drawable itemBackGround = holder.itemView.getResources().getDrawable(R.drawable.background_taskitem);

        switch (task.getColor())
        {
            case 1:
                itemBackGround.setTint(Color.parseColor("#CEEDC7"));
                break;
            case 2:
                itemBackGround.setTint(Color.parseColor("#FF9494"));
                break;
            case 3:
                itemBackGround.setTint(Color.parseColor("#FFC8DD"));
                break;
            case 4:
                itemBackGround.setTint(Color.parseColor("#D7E3FC"));
                break;
            case 5:
                itemBackGround.setTint(Color.parseColor("#FFF6BD"));
                break;
            case 6:
                itemBackGround.setTint(Color.parseColor("#FFD4B2"));
                break;
        }
        holder.itemLayout.setBackground(itemBackGround);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mit= new Intent(tContext, Task_Activity.class);
                mit.putExtra("mytask", (Serializable) task);
                Log.d("EXTRA", mit.getExtras().toString());
                tContext.startActivity(mit);
            }
        });
        holder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (ShowItemLongClickDialog(task) == false)
                    return false;
                else
                {
                    return true;
                }

            }
        });

        holder.taskCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String taskSummary = holder.nameTask.getText().toString();
                String taskDeadline = holder.deadlineTask.getText().toString();

                DateTimeFormatter dmyFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                DateTimeFormatter ymdFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime dmyTaskDeadline = LocalDateTime.parse(taskDeadline, dmyFormat);
                String ymdTaskDeadline = dmyTaskDeadline.format(ymdFormat);

                Log.d("Format datetime in deadline: ", ymdTaskDeadline.toString());

                int rowEffect = -1;

                if (isChecked) {

                    LocalDateTime timeNow = LocalDateTime.now();
                    LocalDateTime startRoundedDateTime = timeNow.with(LocalTime.from(timeNow.toLocalTime().withSecond(timeNow.getSecond()).withNano(0)));
                    String timeNowString = startRoundedDateTime.toString().replace("T", " ");

                    try {
                        rowEffect = CheckTaskFinish(Home_Activity.acc.getEmail().toString(), taskSummary,
                                ymdTaskDeadline.replace("T", " "), timeNowString);
                        TaskDAO taskDAO = new TaskDAO();
                    } catch (Exception e) {
                        Log.d("Mark the task as completed error - UI:", e.getMessage());
                    }
                    if (rowEffect > 0) {
                        Toast.makeText(tContext, "Marked the task as completed successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(tContext, "Marked the task as completed failed", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    try {
                        rowEffect = UnCheckTaskFinish(Home_Activity.acc.getEmail().toString(), taskSummary,
                                ymdTaskDeadline.toString().replace("T", " "));
                        TaskDAO taskDAO = new TaskDAO();
                    } catch (Exception e) {
                        Log.d("Unmarked the task as completed error - UI:", e.getMessage());
                    }
                    if (rowEffect > 0) {
                        Toast.makeText(tContext, "Unmarked the task as completed successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(tContext, "Unmarked the task as completed failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private int CheckTaskFinish(String email, String summary, String deadline, String finish) {
        int rowEffect = -1;

        String query = "EXEC USP_UPDATE_FINISH_TIME_FOR_TASK '" + email + "', N'"
                + summary + "', '" + deadline + "','" + finish + "'";
        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            Log.d("Check task finish: ", query + "\n" + String.valueOf(rowEffect));
        } catch (Exception e) {
            Log.d("Mark the task as completed error - function: ", e.getMessage());
        }
        Log.d("Query is used to update finish time for task: ", query + " \n" + String.valueOf(rowEffect));

        return rowEffect;
    }
    private int UnCheckTaskFinish(String email, String summary, String deadline) {
        int rowEffect = -1;

        String query = "EXEC USP_UPDATE_FINISH_TIME_FOR_TASK '" + email + "', N'"
                + summary + "', '" + deadline + "'," + null + "";
        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            Log.d("Check task finish: ", query + "\n" + String.valueOf(rowEffect));
        } catch (Exception e) {
            Log.d("Mark the task as completed error - function: ", e.getMessage());
        }
        Log.d("Query is used to update finish time for task: ", query + " \n" + String.valueOf(rowEffect));

        return rowEffect;
    }

    private boolean ShowItemLongClickDialog(TaskDTO task) {
        final boolean[] result = new boolean[1];
        Dialog item_dialog;
        Button viewDetailButton, deleteButton;
        item_dialog = new Dialog(tContext);
        item_dialog.setContentView(R.layout.itemlongclick_dialog);
        item_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        item_dialog.getWindow().setBackgroundDrawable(tContext.getDrawable(R.drawable.custom_itemdialog));
        item_dialog.setCancelable(false);

        viewDetailButton = item_dialog.findViewById(R.id.itemDetail_button);
        deleteButton = item_dialog.findViewById(R.id.item_Delete_button);
        ImageButton closeButton = item_dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_dialog.dismiss();
            }
        });
        viewDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result[0] =false;
                Intent mit= new Intent(tContext, Task_Activity.class);
                mit.putExtra("mytask", (Serializable) task);
                Log.d("EXTRA", mit.getExtras().toString());
                tContext.startActivity(mit);
                item_dialog.dismiss();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result[0]=true;
                ShowDeleteDialog(task);
                item_dialog.dismiss();
            }
        });
        item_dialog.show();
        return result[0];
    }

    private boolean ShowDeleteDialog(TaskDTO task) {
        final boolean[] result = new boolean[1];
        Dialog delete_dialog;
        Button cancelButton, deleteButton;
        delete_dialog = new Dialog(tContext);
        delete_dialog.setContentView(R.layout.delete_item_cardview);
        delete_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        delete_dialog.getWindow().setBackgroundDrawable(tContext.getDrawable(R.drawable.custom_itemdialog));
        delete_dialog.setCancelable(false);

        cancelButton = delete_dialog.findViewById(R.id.cancel_button);
        deleteButton = delete_dialog.findViewById(R.id.ok_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result[0] =false;
                delete_dialog.dismiss();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result[0]=true;
                int result = DeleteTask(Home_Activity.acc.getEmail(), task);
                if (result > 0) {
                    Toast.makeText(tContext, "Delete task successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(tContext, "Delete task failed", Toast.LENGTH_SHORT).show();
                }
                taskFragment.ShowListTask();
                delete_dialog.dismiss();
            }
        });
        delete_dialog.show();
        return result[0];
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilterList(List<TaskDTO> filterTask) {
        this.list = filterTask;
        notifyDataSetChanged();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout itemLayout;
        TextView nameTask;
        TextView deadlineTask;
        TextView timeleftTask;
        CheckBox taskCheckbox;
        public TaskViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.item);
            nameTask = itemView.findViewById(R.id.tname_textView);
            deadlineTask = itemView.findViewById(R.id.tDeadline_textView);
            timeleftTask = itemView.findViewById(R.id.tTimeleft_textView);
            taskCheckbox = itemView.findViewById(R.id.checkTaskFinish);
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

        Log.d("list task get", query.toString());

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

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    String endFormatTimeString = end.format(formatter);
                    LocalDateTime endFormatTime = LocalDateTime.parse(endFormatTimeString, formatter);

                    Log.d("Local time for task:", endFormatTime.toString());

                    Duration notification_period = Duration.parse(resultSet.getString(7));
                    String description = resultSet.getString(8);
                    LocalDateTime finish = null;

                    String finishTimeTemp = resultSet.getString(9);
                    if (finishTimeTemp != null) {
                        Timestamp finishTime = resultSet.getTimestamp(9);
                        ZonedDateTime zonedDateTimeFinish = finishTime.toInstant().atZone(ZoneId.systemDefault());
                        finish = zonedDateTimeFinish.toLocalDateTime();
                    }

                    int color = resultSet.getInt(10);

                    TaskDTO task = new TaskDTO(idTask, idUser, name, location, creating, endFormatTime,
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

                    LocalDateTime finish = null;

                    String finishTimeTemp = resultSet.getString(9);
                    if (finishTimeTemp != null) {
                        Timestamp finishTime = resultSet.getTimestamp(9);
                        ZonedDateTime zonedDateTimeFinish = finishTime.toInstant().atZone(ZoneId.systemDefault());
                        finish = zonedDateTimeFinish.toLocalDateTime();
                    }

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

    public List<TaskDTO> getListTaskForNotification(String email, LocalDateTime timeNow) {
        List<TaskDTO> listTasksForNotification = new ArrayList<>();
        LocalDate date = timeNow.toLocalDate();
        LocalTime time = timeNow.toLocalTime();
        String dateTimeNow = date.toString() + " " + time.toString();

        String query = "EXEC USP_GET_TASK_BY_EMAIL_FOR_NOTIFICATION '" + email + "','" + dateTimeNow + "'";

        try {
            ResultSet resultSet = DataProvider.getInstance().executeQuery(query);
            if (resultSet != null) {
                Log.d("ResultSet is : ", "not null" );
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

                    LocalDateTime finish = null;

                    String finishTimeTemp = resultSet.getString(9);
                    if (finishTimeTemp != null) {
                        Timestamp finishTime = resultSet.getTimestamp(9);
                        ZonedDateTime zonedDateTimeFinish = finishTime.toInstant().atZone(ZoneId.systemDefault());
                        finish = zonedDateTimeFinish.toLocalDateTime();
                    }

                    int color = resultSet.getInt(10);

                    TaskDTO task = new TaskDTO(idTask, idUser, name, location, creating, end,
                            notification_period, description, finish, color);
                    listTasksForNotification.add(task);

                    Log.d("Each event of the day for notification: ", task.toString());

                }
            }
        } catch ( Exception e) {
            Log.d("Get list task: ", e.getMessage());
        }

        return  listTasksForNotification;
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

     /*   LocalDateTime finishRoundedDateTime = finish.with(LocalTime.from(finish.toLocalTime().withSecond(finish.getSecond()).withNano(0)));
        LocalDate finishDate = finishRoundedDateTime.toLocalDate();
        LocalTime finishTime = finishRoundedDateTime.toLocalTime();
        String finish_String = finishDate.toString() + " " + finishTime.toString();*/

        String query = "EXEC USP_INSERT_NEW_TASK '" + email + "','" +
                name + "','" + location + "','" + start_String + "','" + end_String + "','" +
                duration + "','" + description + "'," + color;
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedEndTime = endTime.format(formatter);

        String query = "EXEC USP_DELETE_TASK '" + email + "','" +
                name + "','" + formattedEndTime + "'";

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
        int color = task.getColor();

        LocalDateTime creatingRoundedDateTime = creating.with(LocalTime.from(creating.toLocalTime().withSecond(creating.getSecond()).withNano(0)));
        String startTime = creatingRoundedDateTime.toString().replace("T", " ");
        LocalDateTime endRoundedDateTime = end.with(LocalTime.from(end.toLocalTime().withSecond(end.getSecond()).withNano(0)));
        String endTime = endRoundedDateTime.toString().replace("T", " ");

/*        String finishString = task.getFinishedTime().toString();
        String finishTime = null;
        if (finishString != LocalDateTime.MAX.toString()) {
            LocalDateTime finish = task.getFinishedTime();
            LocalDateTime finishRoundedDateTime = finish.with(LocalTime.from(finish.toLocalTime().withSecond(finish.getSecond()).withNano(0)));
            finishTime = finishRoundedDateTime.toString().replace("T", " ");
        }*/

        String query = "EXEC USP_UPDATE_TASK '" + idTask + "','" + name + "','" +
                location + "','" + startTime + "','" + endTime + "','" + notification + "','" +
                description + "'," + color;

        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            Log.d("Update task: ", String.valueOf(rowEffect));
            //Log.d("Update event of the day: ", query);
        }catch (Exception e) {
            Log.d("Update task: ", e.getMessage());
        }

        return rowEffect;
    }

    public int UpdateFinishTask(String email, TaskDTO task) {
        int rowEffect = -1;
        String summary = task.getName();
        String idTask = task.getIdTask();
        LocalDateTime deadline = task.getEndTime();
        LocalDateTime finishTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String deadlineCast = deadline.format(formatter);
        String finishCast = finishTime.format(formatter);

        String query = "EXEC USP_UPDATE_FINISH_TIME_FOR_TASK '" + email + "','" + summary + "','" + summary + "','" + deadlineCast + "','" + finishCast + "'";

        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            Log.d("Update finish time for task: ", String.valueOf(rowEffect));
            //Log.d("Update event of the day: ", query);
        }catch (Exception e) {
            Log.d("Update finish time for task: ", e.getMessage());
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