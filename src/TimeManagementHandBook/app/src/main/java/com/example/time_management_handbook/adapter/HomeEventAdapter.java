package com.example.time_management_handbook.adapter;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContextCompat.startActivity;
import static java.security.AccessController.getContext;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.activity.Event_Activity;
import com.example.time_management_handbook.activity.Task_Activity;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HomeEventAdapter extends BaseAdapter {
    private List<Event_Of_The_Day_DTO> lData;
    private LayoutInflater layoutInflater;
    private Context context;

    public HomeEventAdapter(List<Event_Of_The_Day_DTO> listData, Context aContext) {
        context = aContext;
        lData = listData != null ? listData : new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
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
            convertView = layoutInflater.inflate(R.layout.layout_home_item, null);
            holder = new ViewHolder();
            holder.eventButton = convertView.findViewById(R.id.button_note);
            holder.summaryView = convertView.findViewById(R.id.textView_summary);
            holder.timeView = convertView.findViewById(R.id.textView_time);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Event_Of_The_Day_DTO datas = lData.get(position);
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
                Intent mit= new Intent(context, Task_Activity.class);
                //gửi dữ liệu
                mit.putExtra("myevent", (Serializable) datas);
                Log.d("EXTRA", mit.getExtras().toString());
                context.startActivity(mit);
            }
        });

        return convertView;
    }

    static class ViewHolder{
        Button eventButton;
        TextView summaryView;
        TextView timeView;
    }

}
