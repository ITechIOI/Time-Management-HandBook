package com.example.time_management_handbook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.activity.Event_Activity;
import com.example.time_management_handbook.activity.Task_Activity;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.Prolonged_Event_DTO;
import com.example.time_management_handbook.model.TaskDTO;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Object> lData;
    public static final int EVENT_OF_THE_DAY = 0;
    public static final int PROLONGED_EVENT = 1;
    public static final int TASK = 2;

    public CalendarAdapter(Context context, List<Object> lData) {
        this.context = context;
        this.lData = lData;
    }

    public CalendarAdapter() {}

    @Override
    public int getItemViewType(int position) {
        if (lData.get(position) instanceof Event_Of_The_Day_DTO) {
            return EVENT_OF_THE_DAY;
        }
        else if (lData.get(position) instanceof Prolonged_Event_DTO) {
            return PROLONGED_EVENT;
        }
        else if (lData.get(position) instanceof TaskDTO) {
            return TASK;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        switch (viewType) {
            case EVENT_OF_THE_DAY:
                itemView = inflater.inflate(R.layout.event_item, parent, false);
                return new EventViewHolder(itemView);
            case PROLONGED_EVENT:
                itemView = inflater.inflate(R.layout.event_item, parent, false);
                return new EventViewHolder(itemView);
            case TASK:
                itemView = inflater.inflate(R.layout.task_item, parent, false);
                return new TaskViewHolder(itemView);
            default:
                break;
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        switch (getItemViewType(position)) {
            case EVENT_OF_THE_DAY:
                Event_Of_The_Day_DTO eventOfTheDay = (Event_Of_The_Day_DTO) (lData.get(position));
                EventViewHolder eventOfTheDayViewHolder = (EventViewHolder) holder;

                formatter = DateTimeFormatter.ofPattern("HH:mm");
                eventOfTheDayViewHolder.eventTextView.setText("Event: " + eventOfTheDay.getSummary() + "\nDuration Time: " + eventOfTheDay.getStartTime().format(formatter) + " - " + eventOfTheDay.getEndTime().format(formatter));

                changeLayoutColor(eventOfTheDay.getColor(), eventOfTheDayViewHolder.itemLayout);
                eventOfTheDayViewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mit= new Intent(context, Event_Activity.class);
                        context.startActivity(mit);
                    }
                });
                break;

            case PROLONGED_EVENT:
                Prolonged_Event_DTO prolongedEvent = (Prolonged_Event_DTO) (lData.get(position));
                EventViewHolder prolongedEventViewHolder = (EventViewHolder) holder;

                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                prolongedEventViewHolder.eventTextView.setText("Event: " + prolongedEvent.getSummary() + "\nStart: " + prolongedEvent.getStartDate().format(formatter) + "\nEnd: " + prolongedEvent.getEndDate().format(formatter));

                changeLayoutColor(prolongedEvent.getColor(), prolongedEventViewHolder.itemLayout);
                prolongedEventViewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mit= new Intent(context, Event_Activity.class);
                        context.startActivity(mit);
                    }
                });
                break;

            case TASK:
                TaskDTO task = (TaskDTO) (lData.get(position));
                TaskViewHolder taskViewHolder = (TaskViewHolder) holder;

                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                taskViewHolder.nameTask.setText(task.getName());
                taskViewHolder.deadlineTask.setText(task.getEndTime().toLocalTime().toString());


                if (task.getFinishedTime()!=null)
                    taskViewHolder.taskCheckbox.setChecked(true);
                else taskViewHolder.taskCheckbox.setChecked(false);

                changeLayoutColor(task.getColor(), taskViewHolder.itemLayout);
                taskViewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mit= new Intent(context, Task_Activity.class);
                        context.startActivity(mit);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return lData.size();
    }

    public void changeLayoutColor(int color, RelativeLayout itemLayout){
        switch (color)
        {
            case 1:
                itemLayout.setBackgroundColor(Color.parseColor("#CEEDC7"));
                break;
            case 2:
                itemLayout.setBackgroundColor(Color.parseColor("#FF9494"));
                break;
            case 3:
                itemLayout.setBackgroundColor(Color.parseColor("#FFC8DD"));
                break;
            case 4:
                itemLayout.setBackgroundColor(Color.parseColor("#D7E3FC"));
                break;
            case 5:
                itemLayout.setBackgroundColor(Color.parseColor("#FFF6BD"));
                break;
            case 6:
                itemLayout.setBackgroundColor(Color.parseColor("#FFD4B2"));
                break;
        }
    }
}