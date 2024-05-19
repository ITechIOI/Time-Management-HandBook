package com.example.time_management_handbook.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.time_management_handbook.activity.Home_Activity;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MyForegroundService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private Context context;
    private NotificationManager notificationManager;
    private List<Event_Of_The_Day_DTO> listEventOfTheDay;
    private List<Prolonged_Event_DTO> listProlongedEvent;
    private List<TaskDTO> listTask;
    private Handler handler;

    private static final int NOTIFICATION_ID_BASE = 1;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        handler = new Handler(Looper.getMainLooper());

        // Tạo danh sách các thông báo


        listEventOfTheDay = Home_Activity.listEventOfTheDayForNotification;
        listProlongedEvent = Home_Activity.listProlongedEventForNotification;
        listTask = Home_Activity.listTaskForNotification;

        Log.d("List task for notification in my foreground service OK: ", listProlongedEvent.toString());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        startForeground(NOTIFICATION_ID, createNotification());

        final List<Event_Of_The_Day_DTO> event = listEventOfTheDay;

        if (!event.isEmpty()) {
            for (int i = 0; i < event.size(); i++) {
                final int index = i;

                LocalDateTime today = LocalDateTime.now();
                Duration durationToStartTime = Duration.between(today,event.get(i).getStartTime());
                Duration notification = event.get(i).notification_period;

                Log.d("Duration of event time: ", durationToStartTime.toString() + "   " +
                        notification.toString());

                Duration remainingTime = durationToStartTime.minus(notification);

                Log.d("Remaining time for event:", String.valueOf(remainingTime.getSeconds()));
                Log.d("Remaining time for event:", String.valueOf(remainingTime.toString()));
                if (remainingTime.getSeconds() > 0) {
                    handler.postDelayed(() -> showListEventOfTheDayNotification(index), (long) (remainingTime.getSeconds()) * 1000);
                }
            }
        }

        if (!listProlongedEvent.isEmpty()) {
            for (int i = 0; i < listProlongedEvent.size(); i++) {
                final int index = i;

                LocalDateTime startTimeProlongedEvent = listProlongedEvent.get(i).getStartDate().atTime(LocalTime.of(13, 45, 0));
                LocalDateTime today = LocalDateTime.now();
                Duration durationToStartTime = Duration.between(today, startTimeProlongedEvent);
                Duration notification = listProlongedEvent.get(i).notification_period;
                Duration remainingTime = durationToStartTime.minus(notification);

                Log.d("Remaining time for prolonged event:", String.valueOf(remainingTime.getSeconds()));
                Log.d("Remaining time for prolonged event:", String.valueOf(remainingTime.toString()));

                if (remainingTime.getSeconds() > 0) {
                    handler.postDelayed(() -> showListProlongedEventNotification(index), (long) (remainingTime.getSeconds()) * 1000);
                }
            }
        }

       if (!listTask.isEmpty()) {
           for (int i = 0; i < listTask.size(); i++) {
               final int index = i;
/*
            int days = (int) listTask.get(i).notification_period.toDays();
            int hour = (int) listTask.get(i).notification_period.toHours() % 24;
            int minutes = (int) listTask.get(i).notification_period.toMinutes() % 60;
            int seconds = (int) listTask.get(i).notification_period.getSeconds() % 60;

            handler.postDelayed(() -> showListTaskNotification(index), (long) (86.400 * days +
                    3600 * hour + minutes * 60 + seconds) * 1000);
*/
               LocalDateTime today = LocalDateTime.now();
               Duration durationToEndTime = Duration.between(today, listTask.get(i).getEndTime());

               Duration notification = listTask.get(i).notification_period;
               Duration remainingTime = durationToEndTime.minus(notification);

               Log.d("Remaining time for task:", String.valueOf(remainingTime.getSeconds()));
               Log.d("Remaining time for task:", String.valueOf(remainingTime.toString()));
               handler.postDelayed(() -> showListTaskNotification(index), (long) (remainingTime.getSeconds()) * 1000);
           }
       }

        return START_STICKY;
    }

    private void scheduleShowNewNotificationAfterDelay() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            showNewNotification();
        }, 2000);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            CharSequence name = "Hi guys !";
            String description = "Welcome to my Time Management Handbook";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel serviceChannel = new NotificationChannel("MY_NOTIFICATION_CHANNEL", name, importance);
            serviceChannel.setDescription(description);
            notificationManager.createNotificationChannel(serviceChannel);
        }
    }


    public Notification createNotification() {
        Intent notificationIntent = new Intent(this, Home_Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        Resources res = context.getResources();
        String packageName = context.getPackageName();
        Bitmap icon = BitmapFactory.decodeResource(res, res.getIdentifier("ic_notification", "drawable", packageName));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MY_NOTIFICATION_CHANNEL")
                .setSmallIcon(res.getIdentifier("ic_notification", "drawable", packageName))
                .setContentTitle("Hi guys!!!")
                .setContentText("Welcome to Time Management Handbook")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true);

        return builder.build();
    }

    private void showListEventOfTheDayNotification(int index) {

        Resources res = context.getResources();
        String packageName = context.getPackageName();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MY_NOTIFICATION_CHANNEL")
                .setSmallIcon(res.getIdentifier("ic_notification", "drawable", packageName))
                .setContentTitle(listEventOfTheDay.get(index).summary + "\n"
                        + listEventOfTheDay.get(index).description)
                .setContentText("Deadline:" +  listEventOfTheDay.get(index).startTime.toLocalTime() + "-"
                        + listEventOfTheDay.get(index).endTime.toLocalTime()
                        + "\n" + listEventOfTheDay.get(index).description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true);
        Log.d("Show event of the day notification:",  listEventOfTheDay.get(index).summary );

        notificationManager.notify(NOTIFICATION_ID_BASE + index + 1, builder.build());
    }

    private void showListProlongedEventNotification(int index) {

        Resources res = context.getResources(); // Lấy tài nguyên
        String packageName = context.getPackageName(); // Lấy tên gói ứng dụng

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MY_NOTIFICATION_CHANNEL")
                .setSmallIcon(res.getIdentifier("ic_notification", "drawable", packageName))
                .setContentTitle(listProlongedEvent.get(index).summary + "\n"
                        + listProlongedEvent.get(index).description)
                .setContentText( "Deadline: From " + listProlongedEvent.get(index).startDate + " to " + listProlongedEvent.get(index).endDate +
                        "\n" + listProlongedEvent.get(index).description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true);

        notificationManager.notify(NOTIFICATION_ID_BASE + 2 + index + listEventOfTheDay.size(), builder.build());
    }

    private void showListTaskNotification(int index) {

        Resources res = context.getResources(); // Lấy tài nguyên
        String packageName = context.getPackageName(); // Lấy tên gói ứng dụng

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MY_NOTIFICATION_CHANNEL")
                .setSmallIcon(res.getIdentifier("ic_notification", "drawable", packageName))
                .setContentTitle(listTask.get(index).name + "\n")
                .setContentText("Deadline:" + listTask.get(index).getEndTime().toLocalTime() + " "
                        + listTask.get(index).endTime.toLocalDate() + "\n"
                        + listTask.get(index).description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true);

        notificationManager.notify(NOTIFICATION_ID_BASE + 3 + index + listEventOfTheDay.size() + listProlongedEvent.size(), builder.build());
    }


    private void showNewNotification() {

        Resources res = context.getResources(); // Lấy tài nguyên
        String packageName = context.getPackageName(); // Lấy tên gói ứng dụng

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MY_NOTIFICATION_CHANNEL")
                .setSmallIcon(res.getIdentifier("ic_notification", "drawable", packageName))
                .setContentTitle("New Notification After 5 Minutes")
                .setContentText("This is a new notification shown after 5 minutes.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true);

        // Stop the current foreground service
        stopForeground(false);

        // Start a new foreground service with the new notification
        startForeground(2, builder.build()); // Use a different notification ID for the new notification

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

