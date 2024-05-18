package com.example.time_management_handbook.activity;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.DataProvider;
import com.example.time_management_handbook.adapter.Event_Of_The_Day_DAO;
import com.example.time_management_handbook.retrofit.GoogleAccount;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Login_Activity extends AppCompatActivity {
    private Button loginButton;
    ExecutorService executorService;
    private ExecutorService executorServiceHandle = Executors.newSingleThreadExecutor();
    public static LocalDate today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = GoogleAccount.getInstance(Login_Activity.this).SignInByGoogleAccount(Login_Activity.this);
                startActivityForResult(intent, 1000);*/
                signOutAndChooseAccount();

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            if (ContextCompat.checkSelfPermission(Login_Activity.this,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Login_Activity.this,
                        new String[] {Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }


        today = LocalDate.now();
        executorService = Executors.newSingleThreadExecutor();

        executorService.submit(new Runnable() {
            @Override
            public void run() {

                List<String> users = DataProvider.getInstance().getListUser();
                Log.d("Connect User", users.toString());

            }
        });

       // executorService.shutdown();
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
    private void signOutAndChooseAccount() {
        // Xóa thông tin tài khoản hiện tại
        GoogleSignIn.getClient(this, getGoogleSignInOptions()).signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Người dùng đã đăng xuất thành công, khởi tạo quá trình đăng nhập mới
                            Intent intent = GoogleSignIn.getClient(Login_Activity.this, getGoogleSignInOptions())
                                    .getSignInIntent();
                            int RC_SIGN_IN = 1000;
                            startActivityForResult(intent, RC_SIGN_IN);
                        } else {

                            Log.d("Fail to sign out: ", task.getException().getMessage());
                        }
                    }
                });
    }

    private GoogleSignInOptions getGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }
}
