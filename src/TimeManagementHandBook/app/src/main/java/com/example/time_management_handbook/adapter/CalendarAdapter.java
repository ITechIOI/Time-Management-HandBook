package com.example.time_management_handbook.adapter;

import android.annotation.SuppressLint;
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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.activity.Event_Activity;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.Prolonged_Event_DTO;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Object> lData;
    public static final int EVENT_OF_THE_DAY = 0;
    public static final int PROLONGED_EVENT = 1;

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
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String durationTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        EventViewHolder viewHolder = (EventViewHolder) holder;
        switch (getItemViewType(position)) {
            case EVENT_OF_THE_DAY:
                Event_Of_The_Day_DTO eventOfTheDay = (Event_Of_The_Day_DTO) (lData.get(position));
                formatter = DateTimeFormatter.ofPattern("HH:mm");
                durationTime = eventOfTheDay.getStartTime().format(formatter) + " - " + eventOfTheDay.getEndTime().format(formatter);
                setViewHolder(viewHolder, viewHolder.itemView, eventOfTheDay, eventOfTheDay.getColor(), eventOfTheDay.getSummary(), durationTime);
                break;

            case PROLONGED_EVENT:
                Prolonged_Event_DTO prolongedEvent = (Prolonged_Event_DTO) (lData.get(position));
                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                durationTime = prolongedEvent.getStartDate().format(formatter) + " - " + prolongedEvent.getEndDate().format(formatter);
                setViewHolder(viewHolder, viewHolder.itemView, prolongedEvent, prolongedEvent.getColor(), prolongedEvent.getSummary(), durationTime);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return lData.size();
    }

    public void setViewHolder(EventViewHolder viewHolder, View itemView, Object data, int color, String name, String durationTime){
        viewHolder.eventTextView.setText(name);
        viewHolder.timeEventTextView.setText(durationTime);
        Drawable itemBackGround = itemView.getResources().getDrawable(R.drawable.background_taskitem);
        switch (color) {
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
        viewHolder.itemLayout.setBackground(itemBackGround);
        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mit= new Intent(context, Event_Activity.class);
                mit.putExtra("myevent", (Serializable) data);
                Log.d("EXTRA", mit.getExtras().toString());
                context.startActivity(mit);
            }
        });
        viewHolder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (ShowItemLongClickDialog(data) == false)
                    return false;
                else
                    return true;
            }
        });
    }

    private boolean ShowItemLongClickDialog(Object event) {
        final boolean[] result = new boolean[1];
        Dialog item_dialog;
        Button viewDetailButton, deleteButton;
        item_dialog = new Dialog(context);
        item_dialog.setContentView(R.layout.itemlongclick_dialog);
        item_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        item_dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.custom_itemdialog));
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
                Intent mit= new Intent(context, Event_Activity.class);
                mit.putExtra("myevent", (Serializable) event);
                Log.d("EXTRA", mit.getExtras().toString());
                context.startActivity(mit);
                item_dialog.dismiss();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result[0]=true;
                ShowDeleteDialog();
                item_dialog.dismiss();
            }
        });
        item_dialog.show();
        return result[0];
    }

    private boolean ShowDeleteDialog() {
        final boolean[] result = new boolean[1];
        Dialog delete_dialog;
        Button cancelButton, deleteButton;
        delete_dialog = new Dialog(context);
        delete_dialog.setContentView(R.layout.delete_item_cardview);
        delete_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        delete_dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.custom_itemdialog));
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
                delete_dialog.dismiss();
            }
        });
        delete_dialog.show();
        return result[0];
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