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
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.AccountDAO;
import com.example.time_management_handbook.adapter.Event_Of_The_Day_DAO;
import com.example.time_management_handbook.adapter.Prolonged_Event_DAO;
import com.example.time_management_handbook.adapter.TaskDAO;
import com.example.time_management_handbook.model.CalendarEventDTO;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.MyForegroundService;
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

import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Home_Activity extends AppCompatActivity {

    private static final int REQUEST_CODE_SIGN_IN = 1000;
    private static final int REQUEST_AUTHORIZATION = 1001;
    public static String username;
    public static Uri avatar_uri;
    public static LocalDate today;
    public static GoogleSignInAccount acc;
    public static GoogleSignInAccount accTemp;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ExecutorService executorServiceInsertAccount = Executors.newSingleThreadExecutor();
    private final ExecutorService executorServiceGetUsername = Executors.newSingleThreadExecutor();
    private final ExecutorService executorServiceGetEventOfTheDay = Executors.newSingleThreadExecutor();
    private final ExecutorService executorServiceGetProlongedEvent = Executors.newSingleThreadExecutor();
    private final ExecutorService executorServiceGetTask = Executors.newSingleThreadExecutor();
    private final ExecutorService executorServiceHandleEventOfTheDay = Executors.newSingleThreadExecutor();
    private final ExecutorService executorServiceHandleProlongedEvent = Executors.newSingleThreadExecutor();
    private final ExecutorService executorServiceHandleTask = Executors.newSingleThreadExecutor();
    private final ExecutorService executeServiceInsertEventOfTheDayFetchData = Executors.newSingleThreadExecutor();
    private final ExecutorService executeServiceInsertProlongedEventFetchData = Executors.newSingleThreadExecutor();
    private final ExecutorService executorServiceFetchData = Executors.newSingleThreadExecutor();
    public List<Event> items = new ArrayList<>();
    public List<CalendarEventDTO> calendarEvents = new ArrayList<>();
    private FragmentManager fragmentManager;
    private final Home_Fragment homeFragment = new Home_Fragment();
    private ImageButton avatarButton;
    public static List<Event_Of_The_Day_DTO> listEventOfTheDay = new ArrayList<>();
    public static List<Prolonged_Event_DTO> listProlongedEvent = new ArrayList<>();
    public static List<TaskDTO> listTask = new ArrayList<>();
    public static List<Object> listAll = new ArrayList<>();

    private ExecutorService executorServiceEventOfTheDayForNotificationCreate = Executors.newSingleThreadExecutor();
    private ExecutorService executorServiceTaskForNotificationCreate = Executors.newSingleThreadExecutor();
    private ExecutorService executorServiceEventOfTheDayForNotificationStart = Executors.newSingleThreadExecutor();
    private ExecutorService executorServiceTaskForNotificationStart = Executors.newSingleThreadExecutor();
    private ExecutorService executorServiceProlongedEventForNotificationStart = Executors.newSingleThreadExecutor();
    private ExecutorService executorServiceProlongedEventForNotificationCreate = Executors.newSingleThreadExecutor();
    public static List<Event_Of_The_Day_DTO> listEventOfTheDayForNotification = new ArrayList<>();
    public static List<TaskDTO> listTaskForNotification = new ArrayList<>();

    public static List<Prolonged_Event_DTO>  listProlongedEventForNotification = new ArrayList<>();

    @SuppressLint("NonConstantResourceId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar=findViewById(R.id.home_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();

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

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemID = item.getItemId();
                if (itemID == R.id.nav_home){
                    loadFragment(homeFragment);
                } else if (itemID == R.id.nav_calendar) {
                    loadFragment(new Calendar_Fragment());
                } else if (itemID == R.id.nav_task) {
                    loadFragment(new Task_Fragment());
                } else if (itemID == R.id.nav_setting) {
                    loadFragment(new Setting_Fragment());
                }

                return true;
            }
        });

        // Get google account

        if (accTemp == null) {
            acc = GoogleSignIn.getLastSignedInAccount(this);
        }
        accTemp = acc;
        Log.d("Google sign in account: ", acc.toString());

        final String email = acc.getEmail();
        username = acc.getDisplayName();
        avatar_uri = acc.getPhotoUrl();

        executorServiceInsertAccount.execute(() -> {
            int count_account = AccountDAO.getInstance().InsertNewAccount(email);
            Log.d("Insert new account: ", String.valueOf(count_account));
        });

        // Fetch data from calendar
        executorServiceFetchData.execute(new Runnable() {
            @Override
            public void run() {
                fetchEvents(acc);
            }
        });

        if (acc == null) {

            Intent signInIntent = GoogleAccount.getInstance(Home_Activity.this).SignInByGoogleAccount(Home_Activity.this);
            startActivityForResult(signInIntent, 1000);
        } else {
            String emailLogin = acc.getEmail();
            Toast.makeText(Home_Activity.this, emailLogin, Toast.LENGTH_LONG).show();
        }

        avatarButton = findViewById(R.id.imageButton_avatar);

        if (avatar_uri != null){
            avatarButton.setBackground(Icon.createWithContentUri(avatar_uri).loadDrawable(this));
        }

        if (avatar_uri!= null){
            Glide.with(this)
                    .asBitmap()
                    .load(avatar_uri.toString())
                    .into(new BitmapImageViewTarget(avatarButton) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            avatarButton.setImageBitmap(resource);
                        }
                    });
        } else {
            VectorDrawable vectorDrawable = (VectorDrawable) ContextCompat.getDrawable(this, R.drawable.user_avatar);
            avatarButton.setImageDrawable(vectorDrawable);
        }


        avatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mit= new Intent(Home_Activity.this, Account_Activity.class);
                startActivity(mit);
            }
        });


        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime roundedDateTime = timeNow.with(LocalTime.from(timeNow.toLocalTime().withSecond(timeNow.getSecond()).withNano(0)));
        Log.d("Time now: ", roundedDateTime.toString());
        executorServiceHandleEventOfTheDay.execute(new Runnable() {
            @Override
            public void run() {
                listEventOfTheDay = Event_Of_The_Day_DAO.getInstance().getListEventOfTheDay(acc.getEmail(), roundedDateTime);
                Log.d("List event of the day: ", listEventOfTheDay.toString());
            }
        });

        // Get Prolonged Event

        executorServiceHandleProlongedEvent.execute(new Runnable() {
            @Override
            public void run() {
                listProlongedEvent = Prolonged_Event_DAO.getInstance().getListProlongedEvent(acc.getEmail(), today);
                Log.d("List prolonged event of the day: ", listProlongedEvent.toString());
            }
        });

       // executorServiceHandleProlongedEvent.shutdown();

        // Get Task

        executorServiceHandleTask.execute(new Runnable() {
            @Override
            public void run() {
                listTask = TaskDAO.getInstance().getListTask(acc.getEmail(), roundedDateTime);
                Log.d("List task: ", listTask.toString());
            }
        });
       // executorServiceHandleTask.shutdown();

        listAll = new ArrayList<>();
        listAll.addAll(listEventOfTheDay);
        listAll.addAll(listProlongedEvent);
        listAll.addAll(listTask);
        //listAll.sort(null);

        executorServiceEventOfTheDayForNotificationCreate.execute(new Runnable() {
            @Override
            public void run() {
                listEventOfTheDayForNotification = Event_Of_The_Day_DAO.getInstance().getListEventOfTheDayForNotification(acc.getEmail(), roundedDateTime);
                Log.d("List event for notification: ", listEventOfTheDayForNotification.toString());
            }
        });
       // executorServiceEventOfTheDayForNotificationCreate.shutdown();

        executorServiceTaskForNotificationCreate.execute(new Runnable() {
            @Override
            public void run() {
                listTaskForNotification = TaskDAO.getInstance().getListTaskForNotification(acc.getEmail(), roundedDateTime);
                Log.d("List task for notification: ", listTaskForNotification.toString());
            }
        });
       // executorServiceTaskForNotificationCreate.shutdown();
        executorServiceProlongedEventForNotificationCreate.execute(new Runnable() {
            @Override
            public void run() {
                listProlongedEventForNotification = Prolonged_Event_DAO.getInstance().getListProlongedEventForNotification(acc.getEmail(), roundedDateTime.toLocalDate());
                Log.d("List prolonged event for notification: ", listProlongedEventForNotification.toString());
            }
        });

        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Hàm cần chạy sau 10 giây
                Intent serviceIntent = new Intent(Home_Activity.this, MyForegroundService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);

                } else {
                    startService(serviceIntent);
                }
            }
        };

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnableCode, 2000);

        Log.d("Hello mn, I'm On Activity Create", "ok");

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (accTemp == null) {
            acc = GoogleSignIn.getLastSignedInAccount(this);
        }
        accTemp = acc;

        homeFragment.setHiTextView(username);
        homeFragment.setEventandTaskView(listAll);

        try {
            if (!executorServiceGetUsername.awaitTermination(1, TimeUnit.SECONDS)) {
                // executorServiceGetUsername.shutdownNow();
            }
        } catch (InterruptedException e) {
            // executorServiceGetUsername.shutdownNow();
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
                listEventOfTheDay = Event_Of_The_Day_DAO.getInstance().getListEventOfTheDay(acc.getEmail(), roundedDateTime);
                Log.d("List event of the day: ", listEventOfTheDay.toString());
            }
        });
//        executorServiceGetEventOfTheDay.shutdown();

        // Get Prolonged Event

        executorServiceGetProlongedEvent.execute(new Runnable() {
            @Override
            public void run() {
                listProlongedEvent = Prolonged_Event_DAO.getInstance().getListProlongedEvent(acc.getEmail(), today);
                Log.d("List prolonged event of the day: ", listProlongedEvent.toString());
            }
        });

//        executorServiceGetProlongedEvent.shutdown();

        // Get Task

        executorServiceGetTask.execute(new Runnable() {
            @Override
            public void run() {
                listTask = TaskDAO.getInstance().getListTask(acc.getEmail(), roundedDateTime);
                Log.d("List task: ", listTask.toString());
            }
        });


        executorServiceEventOfTheDayForNotificationStart.execute(new Runnable() {
            @Override
            public void run() {
                listEventOfTheDayForNotification = Event_Of_The_Day_DAO.getInstance().getListEventOfTheDayForNotification(acc.getEmail().toString(), roundedDateTime);
                Log.d("List event for notification: ", listEventOfTheDayForNotification.toString());
            }
        });
        // executorServiceEventOfTheDayForNotificationStart.shutdown();

        executorServiceTaskForNotificationStart.execute(new Runnable() {
            @Override
            public void run() {
                listTaskForNotification = TaskDAO.getInstance().getListTaskForNotification(acc.getEmail().toString(), roundedDateTime);
                Log.d("List task for notification: ", listTaskForNotification.toString());
            }
        });
        // executorServiceTaskForNotificationStart.shutdown();

        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Hàm cần chạy sau 10 giây
                Intent serviceIntent = new Intent(Home_Activity.this, MyForegroundService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);

                } else {
                    startService(serviceIntent);
                }
            }
        };

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnableCode, 2000);

        listAll = new ArrayList<>();
        listAll.addAll(listEventOfTheDay);
        listAll.addAll(listProlongedEvent);
        listAll.addAll(listTask);
        //listAll.sort(null);

        Log.d("Hello mn, I'm Start", "ok");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Hello mn, I'm On Activity Result", "ok");
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Hàm cần chạy sau 10 giây
                Intent serviceIntent = new Intent(Home_Activity.this, MyForegroundService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);

                } else {
                    startService(serviceIntent);
                }
            }
        };

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnableCode, 2000);
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
                    LocalDateTime oneYearAgo = nowLocalDateTime.minusYears(1);
                    LocalDateTime oneYearLater = nowLocalDateTime.plusYears(1);

                    DateTime timeMin = new DateTime(oneYearAgo.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    DateTime timeMax = new DateTime(oneYearLater.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

                    List<String> eventStrings = new ArrayList<String>();
                    Events events = service.events().list("primary")
                            .setMaxResults(10)
                            .setTimeMin(timeMin)
                            .setTimeMax(timeMax)
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
                                    null, summary, location, start, end, durationTime, description, 1);

                            Log.d("List Event of the day from google calendar", eventOfTheDay.toString());
                            executeServiceInsertEventOfTheDayFetchData.execute(() -> {
                                try {
                                    int rowEffect = Event_Of_The_Day_DAO.getInstance().InsertNewEventFromCalendar(acc.getEmail(), eventOfTheDay);
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
                                    int rowEffect = Prolonged_Event_DAO.getInstance().InsertNewProlongedEventFromCalendar(acc.getEmail(), prolongedEvent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
           // executorService.shutdown();
            try {
                if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                   // executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
              //  executorService.shutdownNow();
            }
        }
    }

    private  void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}