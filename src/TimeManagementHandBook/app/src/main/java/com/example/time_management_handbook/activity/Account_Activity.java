package com.example.time_management_handbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.retrofit.GoogleAccount;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Account_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

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
}
