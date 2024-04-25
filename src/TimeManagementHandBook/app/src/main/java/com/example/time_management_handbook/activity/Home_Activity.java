package com.example.time_management_handbook.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.time_management_handbook.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Home_Activity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar=findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        // Tạo logo
        // getSupportActionBar().setLogo(R.drawable.google); // Chưa kịp kiếm hình

        FloatingActionButton fab = findViewById(R.id.float_button_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bsd = new BottomSheetDialog(Home_Activity.this);
                View view = LayoutInflater.from(Home_Activity.this).inflate(R.layout.add_bottomsheetdialog, null);
                bsd.setContentView(view);
                bsd.show();

                Button addEventbt = view.findViewById(R.id.add_event);
                addEventbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mit= new Intent(Home_Activity.this, AddEvent_Activity.class);
                        startActivity(mit);
                    }
                });
                Button addTaskbt = view.findViewById(R.id.add_task);
                addTaskbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mit= new Intent(Home_Activity.this, AddTask_Activity.class);
                        startActivity(mit);
                    }
                });

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        FrameLayout frameLayout = findViewById(R.id.frame_layout);
        loadFragment(new Home_Fragment());
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemID = item.getItemId();
                if (itemID == R.id.nav_home){
                    loadFragment(new Home_Fragment());
                } else if (itemID == R.id.nav_calendar) {
                    loadFragment(new Calendar_Fragment());
                } else if (itemID == R.id.nav_task) {
                    loadFragment(new Task_Fragment());
                } else if (itemID == R.id.nav_setting) {
                    loadFragment(new Setting_Fragment());
                }

                return true;
            }
        });
    }

    // 2 hàm tạo kết nối với account button
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.item_avatar){
            Intent mit= new Intent(Home_Activity.this, Account_Activity.class);
            startActivity(mit);
        }
        return super.onOptionsItemSelected(item);
    }

    private  void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}