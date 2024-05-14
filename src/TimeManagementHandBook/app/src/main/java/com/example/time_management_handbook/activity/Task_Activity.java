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



        /*TaskDTO task = null;
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
        descriptionTextView.setText(task.getDescription());*/

    }
}