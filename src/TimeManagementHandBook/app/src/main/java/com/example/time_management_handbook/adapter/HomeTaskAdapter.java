package com.example.time_management_handbook.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.TaskDTO;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HomeTaskAdapter extends BaseAdapter {
    private final List<TaskDTO> lData;
    private LayoutInflater layoutInflater;
    private Context context;

    public HomeTaskAdapter(List<TaskDTO> listData, Context aContext) {
        context = aContext;
        lData = listData != null ? listData : new ArrayList<>();
        layoutInflater = LayoutInflater.from(aContext);
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

        TaskDTO datas = lData.get(position);
        holder.summaryView.setText(datas.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        holder.timeView.setText(datas.getCreatingTime().format(formatter) + " - " + datas.getEndTime().format(formatter));
        holder.eventButton.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_task_book,0,0);
        holder.eventButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3EDC6")));

        return convertView;
    }

    static class ViewHolder{
        Button eventButton;
        TextView summaryView;
        TextView timeView;
    }

}
