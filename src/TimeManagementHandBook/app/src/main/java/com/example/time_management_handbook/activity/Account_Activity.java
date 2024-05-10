package com.example.time_management_handbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.retrofit.GoogleAccount;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Account_Activity extends AppCompatActivity {
    private int backStackEntryIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = findViewById(R.id.aToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        Bundle extras = getIntent().getExtras();
        if (extras!= null) {
            backStackEntryIndex = extras.getInt("backStackEntryIndex");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button addAccount = findViewById(R.id.button_AddAccount);
        addAccount.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                BottomSheetDialog bsd = new BottomSheetDialog(Account_Activity.this);
                View view = LayoutInflater.from(Account_Activity.this).inflate(R.layout.add_account_bottomsheetdialog, null);
                bsd.setContentView(view);
                bsd.show();

                // Set activity khi nhấn các item của listView (tài khoản có sẵn)

                Button addNewAccount = view.findViewById(R.id.button_AddNewAccount);
                addNewAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mit= new Intent(Account_Activity.this, Login_Activity.class);
                        startActivity(mit);
                    }
                });
            }
        });

        Button logout = findViewById(R.id.button_Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mit= new Intent(Account_Activity.this, Login_Activity.class);
                startActivity(mit);
            }
        });
    }
    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
}
