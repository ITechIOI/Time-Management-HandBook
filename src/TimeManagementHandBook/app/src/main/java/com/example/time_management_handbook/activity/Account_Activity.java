package com.example.time_management_handbook.activity;

import static com.example.time_management_handbook.activity.Home_Activity.avatar_uri;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.retrofit.GoogleAccount;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.Shapeable;

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
//        Bundle extras = getIntent().getExtras();
//        if (extras!= null) {
//            backStackEntryIndex = extras.getInt("backStackEntryIndex");
//        }
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        ShapeableImageView avatar = findViewById(R.id.imageView_avatar);
        if (avatar_uri != null){
            avatar.setImageURI(avatar_uri);
        }
        else avatar.setImageResource(R.drawable.user_avatar);

        TextView userName = findViewById(R.id.textView_username);
        userName.setText(GoogleSignIn.getLastSignedInAccount(this).getDisplayName());

        Button addAccount = findViewById(R.id.button_AddAccount);
        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mit= new Intent(Account_Activity.this, AddAccount_Activity.class);
                Home_Activity.acc = null;
                startActivity(mit);
            }
        });

        Button logout = findViewById(R.id.button_Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mit= new Intent(Account_Activity.this, Login_Activity.class);
                Home_Activity.acc = null;
                startActivity(mit);
            }
        });
    }

//    @Override
//    public void onBackPressed(){
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
//        {
//            getSupportFragmentManager().popBackStack();
//        }
//        else {
//            super.onBackPressed();
//        }
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
