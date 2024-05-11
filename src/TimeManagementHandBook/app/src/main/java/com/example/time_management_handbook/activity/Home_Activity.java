package com.example.time_management_handbook.activity;

import static com.google.api.services.calendar.CalendarScopes.CALENDAR_READONLY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.AccountDAO;
import com.example.time_management_handbook.adapter.Event_Of_The_Day_DAO;
import com.example.time_management_handbook.adapter.Prolonged_Event_DAO;
import com.example.time_management_handbook.adapter.TaskDAO;
import com.example.time_management_handbook.model.CalendarEventDTO;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.Prolonged_Event_DTO;
import com.example.time_management_handbook.model.TaskDTO;
import com.example.time_management_handbook.retrofit.GoogleAccount;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Home_Activity extends AppCompatActivity {

    private static final int REQUEST_CODE_SIGN_IN = 1000;
    private static final int REQUEST_AUTHORIZATION = 1001;
    public static String username;
    public static LocalDate today;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acc;

    List<CalendarEventDTO> events;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ExecutorService executorServiceInsertAccount = Executors.newSingleThreadExecutor();
    private ExecutorService executorServiceGetUsername = Executors.newSingleThreadExecutor();
    private ExecutorService executorServiceGetEventOfTheDay = Executors.newSingleThreadExecutor();
    private ExecutorService executorServiceGetProlongedEvent = Executors.newSingleThreadExecutor();
    private ExecutorService executorServiceGetTask = Executors.newSingleThreadExecutor();
    private ExecutorService executorServiceHandle = Executors.newSingleThreadExecutor();
    public List<Event> items;
    public List<CalendarEventDTO> calendarEvents;
    private TextView hiText;
    private FragmentManager fragmentManager;
    private Home_Fragment homeFragment = new Home_Fragment();

    @SuppressLint("NonConstantResourceId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar=findViewById(R.id.home_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        // Create logo
        // getSupportActionBar().setLogo(R.drawable.google);

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
        loadFragment(homeFragment);

        /// check code in this method
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemID = item.getItemId();
                if (itemID == R.id.nav_home){
                    loadFragment(homeFragment);
                } else if (itemID == R.id.nav_calendar) {
                    loadFragment(new CalendarMonth_Fragment());
                } else if (itemID == R.id.nav_task) {
                    loadFragment(new Task_Fragment());
                } else if (itemID == R.id.nav_setting) {
                    loadFragment(new Setting_Fragment());
                }

                return true;
            }
        });

        // Get google account

        acc = GoogleSignIn.getLastSignedInAccount(this);

        final String email = acc.getEmail();
        username = acc.getDisplayName();
        executorServiceInsertAccount.execute(() -> {
            int count_account = AccountDAO.getInstance().InsertNewAccount(email);
            Log.d("Insert new account: ", String.valueOf(count_account));
        });
        executorServiceInsertAccount.shutdown();

        if (acc == null) {

            Intent signInIntent = GoogleAccount.getInstance(Home_Activity.this).SignInByGoogleAccount(Home_Activity.this);
            startActivityForResult(signInIntent, 1000);
        } else {
            String emailLogin = acc.getEmail();
            Toast.makeText(Home_Activity.this, emailLogin, Toast.LENGTH_LONG).show();
        }

        // Fetch data from Google Calendar

        fetchEvents(acc);

        // Change welcome sentence: Hi + username !

        hiText = findViewById(R.id.textView_Hi);

        executorServiceHandle.execute(new Runnable() {
            @Override
            public void run() {
                LocalDateTime start = LocalDateTime.now();
                LocalDateTime end = LocalDateTime.now();
                Event_Of_The_Day_DTO event = new Event_Of_The_Day_DTO(null,
                        null, "Khong co gi", "Truong", start, end,
                        Duration.parse("P1D"), "Khong co gi", 1
                        );
                int rowEffect = Event_Of_The_Day_DAO.getInstance().InsertNewEvent(acc.getEmail(), event);
                Log.d("Insert new Event", String.valueOf(rowEffect));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        homeFragment.setHiTextView(username + "!");

        try {
            if (!executorServiceGetUsername.awaitTermination(1, TimeUnit.SECONDS)) {
                executorServiceGetUsername.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorServiceGetUsername.shutdownNow();
        }

        // Change current date

        today = LocalDate.now();
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime roundedDateTime = timeNow.with(LocalTime.from(timeNow.toLocalTime().withSecond(timeNow.getSecond()).withNano(0)));
        Log.d("Time now: ", roundedDateTime.toString());

        // Get Event Of The Day
        executorServiceGetEventOfTheDay.execute(new Runnable() {
            @Override
            public void run() {
                List<Event_Of_The_Day_DTO> listEventOfTheDay = Event_Of_The_Day_DAO.getInstance().getListEventOfTheDay(acc.getEmail(), roundedDateTime);
                Log.d("List event of the day: ", listEventOfTheDay.toString());
            }
        });
        executorServiceGetEventOfTheDay.shutdown();

        // Get Prolonged Event

        executorServiceGetProlongedEvent.execute(new Runnable() {
            @Override
            public void run() {
                List<Prolonged_Event_DTO> listProlongedEvent = Prolonged_Event_DAO.getInstance().getListProlongedEvent(acc.getEmail(), today);
                Log.d("List prolonged event of the day: ", listProlongedEvent.toString());
            }
        });

        executorServiceGetProlongedEvent.shutdown();

        // Get Task

        executorServiceGetTask.execute(new Runnable() {
            @Override
            public void run() {
                List<TaskDTO> listTask = TaskDAO.getInstance().getListTask(acc.getEmail(), roundedDateTime);
                Log.d("List task: ", listTask.toString());
            }
        });
    }

    public void fetchEvents(GoogleSignInAccount acc) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
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

                    /*LocalDateTime nowLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());
                    LocalDateTime oneYearLaterLocalDateTime = nowLocalDateTime.plus(Period.ofYears(1));

                    DateTime now = new DateTime(nowLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    DateTime oneYearLater = new DateTime(oneYearLaterLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());*/

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
                        String location = event.getLocation()!= null? event.getLocation().toString() : "No location";
                        String creatorEmail = event.getCreator().getEmail();
                        Duration duration = Duration.ofDays(0).ofHours(0).ofMinutes(10).ofSeconds(0);
                        String startTime = event.getStart().getDateTime()!= null? event.getStart().getDateTime().toString() : event.getStart().getDate().toString();
                        String endTime = event.getEnd().getDateTime()!= null? event.getEnd().getDateTime().toString() : event.getEnd().getDate().toString();



                        CalendarEventDTO calendarEvent = new CalendarEventDTO(
                                event.getId(),
                                event.getSummary(),
                                event.getDescription(),
                                event.getStart().getDateTime(),
                                event.getEnd().getDateTime(),
                                recurrenceInfo,
                                location,
                                duration,
                                creatorEmail
                        );
                        calendarEvents.add(calendarEvent);

                        String eventInfo = String.format("%s (%s) - %s - %s - %s - %s - %s", event.getId(), event.getSummary(), event.getLocation(), event.getDescription(), startTime,endTime, recurrenceInfo);

                        eventStrings.add(eventInfo);
                    }

                    runOnUiThread(() -> {
                        Log.d("List Event", eventStrings.toString());
                    });

                } catch (UserRecoverableAuthIOException userRecoverableException) {
                    startActivityForResult(
                            userRecoverableException.getIntent(), this.REQUEST_AUTHORIZATION);
                }catch (IOException e) {
                    Log.d("Fetch events calendar", "IOException: " + e.getMessage());
                }
            });
            //executorService.shutdown();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }
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