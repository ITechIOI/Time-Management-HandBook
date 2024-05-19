package com.example.time_management_handbook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.Prolonged_Event_DTO;
import com.example.time_management_handbook.model.TaskDTO;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Duration;

public class Event_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Context context = this;

        Toolbar toolbar = findViewById(R.id.eaToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        TextInputEditText tv_event_name = findViewById(R.id.eaName_textInput);
        TextInputEditText tv_event_datestart = findViewById(R.id.eaDateStart_textInput);
        TextInputEditText tv_event_dateend = findViewById(R.id.eaDateEnd_textInput);
        TextInputEditText tv_event_location = findViewById(R.id.eaLocation_textInput);
        TextInputEditText tv_event_description = findViewById(R.id.eaDescription_textInput);
        TextView notificationE = findViewById(R.id.eaNotification_textInput);
        ImageView notificaltion = findViewById(R.id.eaNotification_dialog);
        notificaltion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                NumberPicker day, hour, minute, sec;
                Button buttonOk, buttonCancel;
                dialog.setContentView(R.layout.notification_picker);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
                dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.custom_itemdialog));
                day = dialog.findViewById(R.id.day_picker);
                day.setMinValue(0);
                day.setMaxValue(50);
                hour = dialog.findViewById(R.id.hour_picker);
                hour.setMinValue(0);
                hour.setMaxValue(23);
                minute = dialog.findViewById(R.id.minute_picker);
                minute.setMinValue(0);
                minute.setMaxValue(59);
                sec = dialog.findViewById(R.id.sec_picker);
                sec.setMinValue(0);
                sec.setMaxValue(59);
                buttonOk = dialog.findViewById(R.id.buttonOk);
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text;
                        if (day.getValue()>1)
                            text = day.getValue()+" days ";
                        else
                            text = day.getValue() +" day ";
                        if (hour.getValue()>1)
                            text = text + hour.getValue()+" hours ";
                        else
                            text = text + hour.getValue() +" hour ";
                        if (minute.getValue()>1)
                            text = text + minute.getValue()+" minutes ";
                        else
                            text = text + minute.getValue() +" minute ";
                        if (sec.getValue()>1)
                            text = text + sec.getValue()+" secs ";
                        else
                            text = text + sec.getValue() +" sec ";
                        notificationE.setText(text);
                        dialog.dismiss();
                    }
                });
                buttonCancel = dialog.findViewById(R.id.buttonCancel);
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        //Nhận dữ liệu
        Intent intent = getIntent();
        if (intent.getSerializableExtra("myevent") instanceof Event_Of_The_Day_DTO){
            Event_Of_The_Day_DTO event = (Event_Of_The_Day_DTO) intent.getSerializableExtra("myevent");
            Log.d("TAG", "Received data: " + event);
            tv_event_name.setText(event.getSummary());
            tv_event_datestart.setText(event.getStartTime().toString());
            tv_event_dateend.setText(event.getEndTime().toString());
            tv_event_location.setText(event.getLocation());
            tv_event_description.setText(event.getDescription());
            Duration duration = event.getNotification_period();
            String notification  = "";
            if (duration.toDays() == 0) {
                notification = String.valueOf(duration.toHours() % 24) + "h " + String.valueOf(duration.toMinutes() % 60) +
                        "m " + String.valueOf(duration.getSeconds() % 60) + "s";
            } else {
                notification = String.valueOf(duration.toDays()) + "d " +
                        String.valueOf(duration.toHours() % 24) + "h " + String.valueOf(duration.toMinutes() % 60) +
                        "m " + String.valueOf(duration.getSeconds() % 60) + "s";
            }
            notificationE.setText(notification);
            switch(event.getColor())
            {
                case 1:
                    RadioButton green = findViewById(R.id.green_radiobt);
                    green.setChecked(true);
                    break;
                case 2:
                    RadioButton red = findViewById(R.id.red_radiobt);
                    red.setChecked(true);
                    break;
                case 3:
                    RadioButton pink = findViewById(R.id.pink_radiobt);
                    pink.setChecked(true);
                    break;
                case 4:
                    RadioButton blue = findViewById(R.id.blue_radiobt);
                    blue.setChecked(true);
                    break;
                case 5:
                    RadioButton yellow = findViewById(R.id.yellow_radiobt);
                    yellow.setChecked(true);
                    break;
                case 6:
                    RadioButton orange = findViewById(R.id.orange_radiobt);
                    orange.setChecked(true);
                    break;
            }
        }
        else if (intent.getSerializableExtra("myevent") instanceof Prolonged_Event_DTO){
            Prolonged_Event_DTO event = (Prolonged_Event_DTO) intent.getSerializableExtra("myevent");
            Log.d("TAG", "Received data: " + event);
            tv_event_name.setText(event.getSummary());
            tv_event_datestart.setText(event.getStartDate().toString());
            tv_event_dateend.setText(event.getEndDate().toString());
            tv_event_location.setText(event.getLocation());
            tv_event_description.setText(event.getDescription());
            switch(event.getColor())
            {
                case 1:
                    RadioButton green = findViewById(R.id.green_radiobt);
                    green.setChecked(true);
                    break;
                case 2:
                    RadioButton red = findViewById(R.id.red_radiobt);
                    red.setChecked(true);
                    break;
                case 3:
                    RadioButton pink = findViewById(R.id.pink_radiobt);
                    pink.setChecked(true);
                    break;
                case 4:
                    RadioButton blue = findViewById(R.id.blue_radiobt);
                    blue.setChecked(true);
                    break;
                case 5:
                    RadioButton yellow = findViewById(R.id.yellow_radiobt);
                    yellow.setChecked(true);
                    break;
                case 6:
                    RadioButton orange = findViewById(R.id.orange_radiobt);
                    orange.setChecked(true);
                    break;
            }
        }

        Button saveButton = findViewById(R.id.eaSave_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}