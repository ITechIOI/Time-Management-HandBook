package com.example.time_management_handbook.retrofit;

import static com.google.api.services.calendar.CalendarScopes.CALENDAR_READONLY;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.time_management_handbook.model.CalendarEvent;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoogleCalendarData {
    private static final int REQUEST_CODE_SIGN_IN = 1000;
    private static final Object REQUEST_AUTHORIZATION = 1001;

    public static List<CalendarEvent> calendarEvents;

    public static GoogleCalendarData instance;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private GoogleCalendarData() {}

    public static GoogleCalendarData getInstance(Context context) {
        if (instance == null) {
            instance = new GoogleCalendarData();
        }
        return instance;
    }

    public List<CalendarEvent> fetchEvents(GoogleSignInAccount acc, Context context, Activity activity) {
        try {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    context, Collections.singleton(CALENDAR_READONLY));
            credential.setSelectedAccount(acc.getAccount());

            Calendar service = new Calendar.Builder(
                    new NetHttpTransport(),
                    new GsonFactory(),
                    credential)
                    .setApplicationName("Time Management")
                    .build();

            LocalDateTime nowLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());

            // Thêm một năm vào LocalDateTime
            LocalDateTime oneYearLaterLocalDateTime = nowLocalDateTime.plus(Period.ofYears(1));

            // Chuyển đổi LocalDateTime trở lại DateTime
            DateTime now = new DateTime(nowLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            DateTime oneYearLater = new DateTime(oneYearLaterLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

            List<String> eventStrings = new ArrayList<String>();
            Events events = service.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setTimeMax(oneYearLater)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            List<Event> items = events.getItems();
            calendarEvents = new ArrayList<>();

            Log.d("Handles event", events.toString());

            for (Event event : items) {
                CalendarEvent calendarEvent = new CalendarEvent(
                        event.getId(),
                        event.getSummary(),
                        event.getDescription(),
                        event.getStart().getDateTime(),
                        event.getEnd().getDateTime()
                );
                calendarEvents.add(calendarEvent);
                eventStrings.add(String.format("%s (%s)", event.getSummary(), event.getStart().getDateTime()));
            }
        }catch (UserRecoverableAuthIOException userRecoverableException) {
            return Collections.emptyList();
        }
        catch (IOException e) {
            Log.d("Fetch events calendar", "IOException: " + e.getMessage());
        }
        return calendarEvents;
    }

}
