package com.example.time_management_handbook.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.time_management_handbook.R;

class EventViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout itemLayout;
    public TextView eventTextView;
    public EventViewHolder(@org.checkerframework.checker.nullness.qual.NonNull View itemView) {
        super(itemView);
        itemLayout = itemView.findViewById(R.id.item_event);
        eventTextView = itemView.findViewById(R.id.textView_event);
    }
}

class TaskViewHolder extends RecyclerView.ViewHolder{
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
