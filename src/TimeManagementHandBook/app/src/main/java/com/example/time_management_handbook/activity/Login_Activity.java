package com.example.time_management_handbook.activity;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.DataProvider;
import com.example.time_management_handbook.retrofit.GoogleAccount;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Login_Activity extends AppCompatActivity {
    private Button loginButton;
    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sử dụng GoogleSignIn.getClient để khởi tạo GoogleSignInClient
                Intent intent = GoogleAccount.getInstance(Login_Activity.this).SignInByGoogleAccount(Login_Activity.this);
                startActivityForResult(intent, 1000);
            }
        });

        executorService = Executors.newSingleThreadExecutor();

        executorService.submit(new Runnable() {
            @Override
            public void run() {

                List<String> users = DataProvider.getInstance().getListTeacher(DataProvider.getInstance().getConnection());

                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Thực hiện hoạt động UI ở đây
                        // Ví dụ: hiển thị toast
                        Toast.makeText(Login_Activity.this, DataProvider.getInstance().getListTeacher(DataProvider.getInstance().getConnection()).toString(), Toast.LENGTH_LONG).show();
                        Log.d("Connect to Database", users.toString());
                    }
                });*/
            }
        });

        executorService.shutdown();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                if (task.isSuccessful()) {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    if (account!= null) {
                        NavigationToAnotherActivity();
                    } else {
                        Log.d("ErrorX", "signInResult: account is null");
                    }
                } else {
                    Log.d("ErrorY", "signInResult:failed code=" + ((ApiException) task.getException()).getStatusCode());
                    Toast.makeText(Login_Activity.this, "Đăng nhập không thành công", Toast.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                Log.d("SignInError", "signInResult:failed code=" + e.getStatusCode());
                Toast.makeText(Login_Activity.this, "Error signing in: " + e.getStatusCode(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void NavigationToAnotherActivity() {
        Intent intent = new Intent(Login_Activity.this, Home_Activity.class);
        startActivity(intent);
    }
}
