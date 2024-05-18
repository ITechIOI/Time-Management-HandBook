package com.example.time_management_handbook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.model.TaskDTO;

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
        LinearLayout notificationLayout = findViewById(R.id.taNotification_Layout);
        TextView descriptionTextView = findViewById(R.id.taDescription_textInput);
        Button saveButton = findViewById(R.id.taSave_button);
        nameTextView.setText(task.getName());
        deadlineTextView.setText(task.getEndTime().toString());
        locationTextView.setText(task.getLocation());
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
        descriptionTextView.setText(task.getDescription());
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}