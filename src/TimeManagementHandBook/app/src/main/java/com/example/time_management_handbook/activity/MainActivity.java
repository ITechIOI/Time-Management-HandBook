package com.example.time_management_handbook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.DataProvider;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Button button;
    Button connect;
    ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);
        connect = findViewById(R.id.button2);
        executorService = Executors.newSingleThreadExecutor();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mit= new Intent(MainActivity.this, Home_Activity.class);
                startActivity(mit);
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        final Connection connectionString = DataProvider.getInstance().getConnection();
                        // Sử dụng runOnUiThread để cập nhật UI từ thread khác
                        final List<String> listResult = DataProvider.getInstance().getListTeacher(connectionString);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, listResult.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}