package com.example.time_management_handbook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.DataProvider;
import com.example.time_management_handbook.retrofit.GoogleAccount;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
        startActivity(intent);
        finish();
        //welcomeScreen();
    }

    private void welcomeScreen(){
        int time = 1000;
        new Handler().postDelayed(()->{
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            startActivity(intent);
            finish();
        },time);
    }

    // Dưới đây là phần cũ
    /*
    Button button;
    Button connect;

    Button login;
    ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);
        connect = findViewById(R.id.button2);
        executorService = Executors.newSingleThreadExecutor();
        login = findViewById(R.id.loginBtn);

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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent googleIntent = GoogleAccount.getInstance().SignInByGoogleAccount(MainActivity.this);
                startActivityForResult(googleIntent, 1000);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                if (task.isSuccessful()) {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    // Kiểm tra xem account có phải là null hay không
                    if (account != null) {
                        NavigationToAnotherActivity();
                    } else {
                        // Xử lý trường hợp đăng nhập không thành công
                        Log.d("ErrorX", "signInResult: account is null");
                    }
                } else {
                    // Xử lý trường hợp đăng nhập không thành công
                    Log.d("ErrorY", "signInResult:failed code=" + ((ApiException) task.getException()).getStatusCode());
                    Toast.makeText(MainActivity.this, "Đăng nhập không thành công", Toast.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                Log.d("ErrorY", "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    public void NavigationToAnotherActivity() {
        finish();
        Intent intent = new Intent(MainActivity.this, Home_Activity.class);
        startActivityForResult(intent, 1000);
    }
     */
}