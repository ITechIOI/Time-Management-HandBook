package com.example.time_management_handbook.retrofit;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.time_management_handbook.activity.MainActivity;
import com.example.time_management_handbook.adapter.DataProvider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleAccount {
    public static GoogleAccount instance;
    public static GoogleSignInClient mGoogleSignInClient;
    public static GoogleSignInOptions gso;

    private GoogleAccount(Context context) {}


    public static GoogleAccount getInstance(Context context) {
        if (instance == null) {
            instance = new GoogleAccount(context);
        }
        return instance;
    }

    public GoogleSignInClient getmGoogleSignInClient(Context context) {
        if (mGoogleSignInClient == null) {
            mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        }
        return mGoogleSignInClient;
    }

    public Intent SignInByGoogleAccount(Context context) {
        // Tạo GoogleSignInOptions với quyền truy cập cần thiết
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

// Tạo GoogleSignInClient với GoogleSignInOptions
         mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

// Khởi động quy trình đăng nhập
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        return signInIntent;
    }

}
