package com.example.time_management_handbook.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.time_management_handbook.R;

import java.util.List;

public class CustomGridAdapter extends BaseAdapter {
    private List<Event_Of_The_Day_DTO> lData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomGridAdapter(List<Event_Of_The_Day_DTO> listData, Context aContext) {
        context = aContext;
        lData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return lData.size();
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
            holder.eventButton = (Button) convertView.findViewById(R.id.button_note);
            holder.timeView = (TextView) convertView.findViewById(R.id.textView_time);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Event_Of_The_Day_DTO events = lData.get(position);
        holder.eventButton.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_event,0,0);
        holder.eventButton.setText(events.getSummary());
        holder.timeView.setText(events.getStartTime() + " - " + events.getEndTime());
        return convertView;
    }

    static class ViewHolder{
        Button eventButton;
        TextView timeView;
    }

}
