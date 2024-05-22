package com.example.time_management_handbook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.activity.Calendar_Fragment;
import com.example.time_management_handbook.model.CalendarModel;

import java.util.List;

public class CalendarModelAdapter extends RecyclerView.Adapter<CalendarModelAdapter.CalendarViewHolder> {
    private List<CalendarModel> list;
    private int select;
    private Context context;
    private Calendar_Fragment calendarFragment;

    public CalendarModelAdapter(List<CalendarModel> list, Context context, Calendar_Fragment calendarFragment)
    {
        this.list = list;
        this.context = context;
        this.calendarFragment = calendarFragment;
        select = -1;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_date_layout,parent,false);
        ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        CalendarModel model = (CalendarModel) list.get(position);
        int pos = position;
        holder.dateText.setText(String.valueOf(model.getDay()));
        if (select == pos)
            holder.itemView.setBackground(context.getDrawable(R.drawable.selected));
        else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            switch (model.getState())
            {
                case -1:
                    holder.itemView.setVisibility(View.INVISIBLE);
                    break;
                case 0:
                    holder.itemView.setBackground(context.getDrawable(R.drawable.event));
                    break;
                case 1:
                    holder.itemView.setBackground(context.getDrawable(R.drawable.today_item));
                    break;
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(select);
                select = pos;
                notifyItemChanged(pos);
                calendarFragment.selectedDateChange(model.getDate());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class CalendarViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout dateLayout;
        TextView dateText;
        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dateLayout = itemView.findViewById(R.id.date_layout);
            dateText = itemView.findViewById(R.id.dayText);
        }
    }
}
