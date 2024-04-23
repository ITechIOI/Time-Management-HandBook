package com.example.time_management_handbook.retrofit;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.Intent;

import com.example.time_management_handbook.adapter.DataProvider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleAccount {
    public static GoogleAccount instance;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    private GoogleAccount() {}

    public static GoogleAccount getInstance() {
        if (instance == null) {
            instance = new GoogleAccount();
        }
        return instance;
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }
    public void setmGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
    }

    public Intent SignInByGoogleAccount(Context context) {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        return signInIntent;
    }
}
