package com.example.time_management_handbook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.model.TaskDTO;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Task_Activity extends AppCompatActivity {
    private int backStackEntryIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.taToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        //toolbar.setNavigationOnClickListener(v -> finish());


        TaskDTO task = null;
        Intent intent = getIntent();
        task = (TaskDTO) intent.getSerializableExtra("mytask");
        Log.d("TAG", "Received data: " + task);
        if (task == null){return;}
        TextView nameTextView = findViewById(R.id.taName_textInput);
        TextView deadlineTextView = findViewById(R.id.taDeadline_textInput);
        TextView locationTextView = findViewById(R.id.taLocation_textInput);
        RadioGroup colorRadio = findViewById(R.id.taColor_radio);
        TextView notificationT = findViewById(R.id.taNotification_textInput);
        ImageView notificaltion = findViewById(R.id.taNotification_dialog);
        TextView descriptionTextView = findViewById(R.id.taDescription_textInput);
        Button saveButton = findViewById(R.id.taSave_button);

        LocalDateTime taskDeadline = task.getEndTime();
        LocalDate taskDeadlineDate = taskDeadline.toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedTaskDeadlineDate = taskDeadlineDate.format(formatter);
        LocalTime taskDeadlineTime = taskDeadline.toLocalTime();
        Context context = this;
        Log.d("Deadline for task today: ",formattedTaskDeadlineDate.toString() + " " +
                taskDeadlineTime);

        locationTextView.setText(task.getLocation());
        nameTextView.setText(task.getName());
        deadlineTextView.setText(formattedTaskDeadlineDate.toString() + " " +
                taskDeadlineTime);
        Duration duration = task.getNotification_period();
        String notification  = "";
        if (duration.toDays() == 0) {
            notification = String.valueOf(duration.toHours() % 24) + "h " + String.valueOf(duration.toMinutes() % 60) +
                    "m " + String.valueOf(duration.getSeconds() % 60) + "s";
        } else {
            notification = String.valueOf(duration.toDays()) + "d " +
                    String.valueOf(duration.toHours() % 24) + "h " + String.valueOf(duration.toMinutes() % 60) +
                    "m " + String.valueOf(duration.getSeconds() % 60) + "s";
        }
        notificationT.setText(notification);

        switch(task.getColor())
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
                        String text = day.getValue() +"d "+hour.getValue()+"h "+minute.getValue()+"m "+sec.getValue()+"s";
                        notificationT.setText(text);
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
        descriptionTextView.setText(task.getDescription());
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Save successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }
}