package com.example.time_management_handbook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.activity.Event_Activity;
import com.example.time_management_handbook.activity.Task_Activity;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.Prolonged_Event_DTO;
import com.example.time_management_handbook.model.TaskDTO;

import java.io.Serializable;
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
                return new TaskDAO.TaskViewHolder(itemView);
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
                eventOfTheDayViewHolder.eventTextView.setText(eventOfTheDay.getSummary()  );
                eventOfTheDayViewHolder.timeEventTextView.setText(eventOfTheDay.getStartTime().format(formatter) + " - " + eventOfTheDay.getEndTime().format(formatter));
                changeLayoutColor(eventOfTheDay.getColor(), eventOfTheDayViewHolder.itemView, eventOfTheDayViewHolder.itemLayout);
                eventOfTheDayViewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mit= new Intent(context, Event_Activity.class);
                        mit.putExtra("mytask", (Serializable) eventOfTheDay);
                        Log.d("EXTRA", mit.getExtras().toString());
                        context.startActivity(mit);
                    }
                });
                break;

            case PROLONGED_EVENT:
                Prolonged_Event_DTO prolongedEvent = (Prolonged_Event_DTO) (lData.get(position));
                EventViewHolder prolongedEventViewHolder = (EventViewHolder) holder;

                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                prolongedEventViewHolder.eventTextView.setText(prolongedEvent.getSummary());
                prolongedEventViewHolder.timeEventTextView.setText(prolongedEvent.getStartDate().format(formatter) + " - " + prolongedEvent.getEndDate().format(formatter));
                changeLayoutColor(prolongedEvent.getColor(),prolongedEventViewHolder.itemView, prolongedEventViewHolder.itemLayout);
                prolongedEventViewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mit= new Intent(context, Event_Activity.class);
                        mit.putExtra("mytask", (Serializable) prolongedEvent);
                        Log.d("EXTRA", mit.getExtras().toString());
                        context.startActivity(mit);
                    }
                });
                break;

            case TASK:
                TaskDTO task = (TaskDTO) (lData.get(position));
                TaskDAO.TaskViewHolder taskViewHolder = (TaskDAO.TaskViewHolder) holder;

                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                taskViewHolder.nameTask.setText(task.getName());
                taskViewHolder.deadlineTask.setText(task.getEndTime().toLocalTime().toString());
                

                if (task.getFinishedTime()!=null)
                    taskViewHolder.taskCheckbox.setChecked(true);
                else taskViewHolder.taskCheckbox.setChecked(false);

                changeLayoutColor(task.getColor(),taskViewHolder.itemView, taskViewHolder.itemLayout);
                taskViewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mit= new Intent(context, Task_Activity.class);
                        mit.putExtra("mytask", (Serializable) task);
                        Log.d("EXTRA", mit.getExtras().toString());
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

    public void changeLayoutColor(int color, View itemView,RelativeLayout itemLayout){
        Drawable itemBackGround = itemView.getResources().getDrawable(R.drawable.background_taskitem);
        switch (color)
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
        itemLayout.setBackground(itemBackGround);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout itemLayout;
        public TextView eventTextView;
        public TextView timeEventTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.item_event);
            eventTextView = itemView.findViewById(R.id.textView_event);
            timeEventTextView = itemView.findViewById(R.id.eventTime_textview);
        }
    }
}