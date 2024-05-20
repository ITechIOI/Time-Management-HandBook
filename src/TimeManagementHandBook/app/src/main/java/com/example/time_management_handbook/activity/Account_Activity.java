package com.example.time_management_handbook.activity;

import static com.example.time_management_handbook.activity.Home_Activity.avatar_uri;
import static com.google.api.services.calendar.CalendarScopes.CALENDAR_READONLY;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.Event_Of_The_Day_DAO;
import com.example.time_management_handbook.adapter.Prolonged_Event_DAO;
import com.example.time_management_handbook.model.CalendarEventDTO;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.Prolonged_Event_DTO;
import com.example.time_management_handbook.retrofit.GoogleAccount;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.Shapeable;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Account_Activity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private int backStackEntryIndex;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ExecutorService executeServiceInsertEventOfTheDayFetchData = Executors.newSingleThreadExecutor();
    private ExecutorService  executeServiceInsertProlongedEventFetchData = Executors.newSingleThreadExecutor();
    private ExecutorService executorServiceFetchData = Executors.newSingleThreadExecutor();
    private GoogleSignInAccount accountSynchronize;
    private Button synchronizeBtn;
    public List<CalendarEventDTO> calendarEvents = new ArrayList<>();
    public List<Event> items = new ArrayList<>();
    private static final int REQUEST_CODE_SIGN_IN = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = findViewById(R.id.aToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        ShapeableImageView avatar = findViewById(R.id.imageView_avatar);
        if (avatar_uri != null){
            avatar.setImageURI(avatar_uri);
        }
        else avatar.setImageResource(R.drawable.user_avatar);

        TextView userName = findViewById(R.id.textView_username);
        userName.setText(Home_Activity.acc.getDisplayName());

        synchronizeBtn = findViewById(R.id.button_AddAccount);
        synchronizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutAndChooseAccount();
            }
        });

        Button logout = findViewById(R.id.button_Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mit= new Intent(Account_Activity.this, Login_Activity.class);
                Home_Activity.acc = null;
                Home_Activity.accTemp = null;
                startActivity(mit);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                if (task.isSuccessful()) {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    accountSynchronize = account;

                    executorServiceFetchData.execute(new Runnable() {
                        @Override
                        public void run() {

                            fetchEvents(accountSynchronize);

                        }
                    });

                    Log.d("Account synchronize: ", accountSynchronize.getEmail().toString());
                    Home_Activity.acc = Home_Activity.accTemp;
                    if (account!= null) {
                        Log.d("Synchronize account: ", accountSynchronize.toString());
                    } else {
                        Log.d("ErrorX", "signInResult: account is null");
                    }
                } else {
                    Log.d("ErrorY", "signInResult:failed code=" + ((ApiException) task.getException()).getStatusCode());
                    Toast.makeText(Account_Activity.this, "Đăng nhập không thành công", Toast.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                Log.d("SignInError", "signInResult:failed code=" + e.getStatusCode());
                Toast.makeText(Account_Activity.this, "Error signing in: " + e.getStatusCode(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void signOutAndChooseAccount() {
        // Xóa thông tin tài khoản hiện tại
        GoogleSignIn.getClient(this, getGoogleSignInOptions()).signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Người dùng đã đăng xuất thành công, khởi tạo quá trình đăng nhập mới
                            Intent intent = GoogleSignIn.getClient(Account_Activity.this, getGoogleSignInOptions())
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

    public void fetchEvents(GoogleSignInAccount acc) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, REQUEST_CODE_SIGN_IN);
        } else {
            executorService.execute(() -> {
                try {
                    GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                            this, Collections.singleton(CALENDAR_READONLY));
                    credential.setSelectedAccount(acc.getAccount());

                    Calendar service = new Calendar.Builder(
                            new NetHttpTransport(),
                            new GsonFactory(),
                            credential)
                            .setApplicationName("Time Management")
                            .build();

                    LocalDateTime nowLocalDateTime = LocalDateTime.now();

                    // Tính toán thời điểm bắt đầu (timeMin) là một năm trước thời điểm hiện tại
                    LocalDateTime oneYearAgo = nowLocalDateTime.minusYears(1);
                    // Tính toán thời điểm kết thúc (timeMax) là một năm sau thời điểm hiện tại
                    LocalDateTime oneYearLater = nowLocalDateTime.plusYears(1);

                    // Chuyển đổi LocalDateTime trở lại DateTime
                    DateTime timeMin = new DateTime(oneYearAgo.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    DateTime timeMax = new DateTime(oneYearLater.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

                    List<String> eventStrings = new ArrayList<String>();
                    Events events = service.events().list("primary")
                            .setMaxResults(10)
                            .setTimeMin(timeMin)
                            .setTimeMax(timeMax)
                            //.setOrderBy("startTime")
                            .setSingleEvents(true)
                            .execute();

                    items = events.getItems();
                    calendarEvents = new ArrayList<>();

                    Log.d("Handles event", items.toString());

                    for (Event event : items) {
                        String recurrenceInfo = event.getRecurrence()!= null? event.getRecurrence().toString() : "No recurrence";
                        String location = event.getLocation()!= null? event.getLocation() : "No location";
                        String creatorEmail = event.getCreator().getEmail();
                        Duration durationTime = Duration.ofDays(0).ofHours(0).ofMinutes(10).ofSeconds(1);
                        String startTime = event.getStart().getDateTime()!= null? event.getStart().getDateTime().toString() : event.getStart().getDate().toString();
                        String endTime = event.getEnd().getDateTime()!= null? event.getEnd().getDateTime().toString() : event.getEnd().getDate().toString();
                        String description = event.getDescription();
                        String summary = event.getSummary();

                        CalendarEventDTO calendarEvent = new CalendarEventDTO(
                                event.getId(),
                                event.getSummary(),
                                event.getDescription(),
                                event.getStart().getDateTime(),
                                event.getEnd().getDateTime(),
                                recurrenceInfo,
                                location,
                                durationTime,
                                creatorEmail
                        );

                        if (startTime.length() > 10) {
                            Log.d("Start time calendar: ", startTime + "  " + endTime);

                            LocalDateTime start = null, end = null;
                            try {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                                start = LocalDateTime.parse(startTime, formatter);
                                end = LocalDateTime.parse(endTime, formatter);
                            } catch (DateTimeParseException e) {
                                Log.d("Parse date: ", e.getMessage());
                            }
                            Log.d("Calendar Date: ", start.toString() + " " + end.toString());

                            Event_Of_The_Day_DTO eventOfTheDay = new Event_Of_The_Day_DTO(null,
                                    null, summary, location, start, end, durationTime, description, 2);

                            Log.d("List Event of the day from google calendar", eventOfTheDay.toString());
                            executeServiceInsertEventOfTheDayFetchData.execute(() -> {
                                try {
                                    int rowEffect = Event_Of_The_Day_DAO.getInstance().InsertNewEventFromCalendar(Home_Activity.acc.getEmail(), eventOfTheDay);
                                    if (rowEffect > 0) {
                                        Log.d("Insert event of the day from google calendar", "success");
                                    } else {
                                        Log.d("Insert event of the day from google calendar", "failed");
                                    }
                                } catch (Exception e) {
                                    Log.d("Insert event of the day from google calendar", e.getMessage());
                                }
                            });
                        } else {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate start = LocalDate.parse(startTime, formatter);
                            LocalDate end = LocalDate.parse(endTime, formatter);

                            Prolonged_Event_DTO prolongedEvent = new Prolonged_Event_DTO(null, null,
                                    summary, location, start, end, durationTime, description, 2);
                            Log.d("List Prolonged event from google calendar", prolongedEvent.toString());
                            executeServiceInsertProlongedEventFetchData.execute(() -> {
                                try {
                                    int rowEffect = Prolonged_Event_DAO.getInstance().InsertNewProlongedEventFromCalendar(Home_Activity.acc.getEmail(), prolongedEvent);
                                    if (rowEffect > 0) {
                                        Log.d("Insert prolonged event from google calendar", "success");
                                    } else {
                                        Log.d("Insert prolonged event from google calendar", "failed");
                                    }
                                } catch (Exception e) {
                                    Log.d("Insert prolonged event from google calendar", e.getMessage());
                                }
                            });
                        }

                        calendarEvents.add(calendarEvent);

                        String eventInfo = String.format("%s (%s) - %s - %s - %s - %s - %s", event.getId(), event.getSummary(), event.getLocation(), event.getDescription(), startTime, endTime, recurrenceInfo);

                        eventStrings.add(eventInfo);
                    }

                    runOnUiThread(() -> {
                        Log.d("List Event Calendar", eventStrings.toString());
                        Toast.makeText(Account_Activity.this,"Synchronize successfully", Toast.LENGTH_SHORT).show();
                    });

                } catch (UserRecoverableAuthIOException userRecoverableException) {
                    startActivityForResult(
                            userRecoverableException.getIntent(), REQUEST_AUTHORIZATION);
                } catch (IOException e) {
                    Log.d("Fetch events calendar", "IOException: " + e.getMessage());
                }
            });
        }
    }




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
