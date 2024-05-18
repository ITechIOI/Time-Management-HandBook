package com.example.time_management_handbook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.TaskDTO;

public class Event_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar toolbar = findViewById(R.id.eaToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        //Nhận dữ liệu
        Event_Of_The_Day_DTO event = null;
        Intent intent = getIntent();
        event = (Event_Of_The_Day_DTO) intent.getSerializableExtra("myevent");
        Log.d("TAG", "Received data: " + event);
        //Hiển thị dữ liệu
        if (event == null){return;}

    }
}