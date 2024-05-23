package com.example.time_management_handbook.adapter;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.activity.Event_Activity;
import com.example.time_management_handbook.activity.Task_Activity;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.Prolonged_Event_DTO;
import com.example.time_management_handbook.model.TaskDTO;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends BaseAdapter {
    private List<Object> lData;
    private Context context;

    public HomeAdapter(List<Object> listData, Context aContext) {
        context = aContext;
        lData = listData != null ? listData : new ArrayList<>();

    }

    @Override
    public int getCount() {
        return (lData != null) ? lData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return lData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_item, parent, false);
            holder = new ViewHolder();
            holder.eventButton = convertView.findViewById(R.id.button_note);
            holder.summaryView = convertView.findViewById(R.id.textView_summary);
            holder.timeView = convertView.findViewById(R.id.textView_time);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (lData.get(position) instanceof Event_Of_The_Day_DTO){
            Event_Of_The_Day_DTO datas = (Event_Of_The_Day_DTO) lData.get(position);
            holder.summaryView.setText(datas.getSummary());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            holder.timeView.setText(datas.getStartTime().format(formatter) + " - " + datas.getEndTime().format(formatter));
            holder.eventButton.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_event,0,0);
            switch (datas.getColor()) {
                case 1:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CEEDC7")));
                    break;
                case 2:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF9494")));
                    break;
                case 3:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFC8DD")));
                    break;
                case 4:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D7E3FC")));
                    break;
                case 5:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF6BD")));
                    break;
                case 6:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD4B2")));
                    break;
            }

            holder.eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mit= new Intent(context, Event_Activity.class);
                    //gửi dữ liệu
                    mit.putExtra("myevent", (Serializable) datas);
                    Log.d("EXTRA", mit.getExtras().toString());
                    context.startActivity(mit);
                }
            });
        }
        else if (lData.get(position) instanceof Prolonged_Event_DTO){
            Prolonged_Event_DTO datas = (Prolonged_Event_DTO) lData.get(position);
            holder.summaryView.setText(datas.getSummary());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            holder.timeView.setText("Start: " + datas.getStartDate().format(formatter) + "\nEnd: " + datas.getEndDate().format(formatter));
            holder.eventButton.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_event,0,0);
            switch (datas.getColor()) {
                case 1:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CEEDC7")));
                    break;
                case 2:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF9494")));
                    break;
                case 3:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFC8DD")));
                    break;
                case 4:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D7E3FC")));
                    break;
                case 5:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF6BD")));
                    break;
                case 6:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD4B2")));
                    break;
            }

            holder.eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mit= new Intent(context, Event_Activity.class);
                    //gửi dữ liệu
                    mit.putExtra("myevent", (Serializable) datas);
                    Log.d("EXTRA", mit.getExtras().toString());
                    context.startActivity(mit);
                }
            });
        }

        else if (lData.get(position) instanceof TaskDTO){
            TaskDTO datas = (TaskDTO) lData.get(position);
            holder.summaryView.setText(datas.getName());
            // Dinh dang thoi gian deadline
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            Duration duration = Duration.between(LocalDateTime.now(), datas.getEndTime());
            String remainingTime  = "";
            if (duration.toDays() == 0) {
                remainingTime = String.valueOf(duration.toHours() % 24) + "h " + String.valueOf(duration.toMinutes() % 60) +
                        "m " + String.valueOf(duration.getSeconds() % 60) + "s";
            } else {
                remainingTime = String.valueOf(duration.toDays()) + "d " +
                        String.valueOf(duration.toHours() % 24) + "h " + String.valueOf(duration.toMinutes() % 60) +
                        "m " + String.valueOf(duration.getSeconds() % 60) + "s";
            }
            holder.timeView.setText("Deadline: " + datas.getEndTime().format(formatter) + "\nRemaining time: " +remainingTime);
            holder.eventButton.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_task_book,0,0);
            switch (datas.getColor())
            {
                case 1:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CEEDC7")));
                    break;
                case 2:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF9494")));
                    break;
                case 3:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFC8DD")));
                    break;
                case 4:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D7E3FC")));
                    break;
                case 5:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF6BD")));
                    break;
                case 6:
                    holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD4B2")));
                    break;
            }
            holder.eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mit= new Intent(context, Task_Activity.class);
                    mit.putExtra("mytask", (Serializable) datas);
                    Log.d("EXTRA", mit.getExtras().toString());
                    context.startActivity(mit);
                }
            });
        }
        return convertView;
    }

    static class ViewHolder{
        Button eventButton;
        TextView summaryView;
        TextView timeView;
    }

}
