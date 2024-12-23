package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class TemperatureService extends Service {
    private FirebaseFirestore db;
    private long lastNotificationTime=0;

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();
        createNotificationChannel();
        startForeground(1, createNotification("Service started"));
        listenToTemperatureChanges();
        lastNotificationTime=0;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void listenToTemperatureChanges() {
        DocumentReference documentReference = db.collection("theSensor").document("Real_Time");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    String tempStr = value.getString("Temperature");
                    if (tempStr != null) {
                        float temperature = Float.parseFloat(tempStr);
                        if (temperature > 34.01) {
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - lastNotificationTime > 60000) { // 60000 ms = 1 minute
                                sendNotification("Temperature exceeded 34 degrees!");
                                lastNotificationTime = currentTime;
                            }
                        }
                    }
                }
            }
        });
    }

    private void sendNotification(String message) {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "temperatureAlert")
                .setSmallIcon(R.drawable.icontemperaturealert)
                .setContentTitle("Temperature Alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Temperature Alert Channel";
            String description = "Channel for Temperature Alerts";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("temperatureAlert", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification(String contentText) {
        return new NotificationCompat.Builder(this, "temperatureAlert")
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle("Temperature Service")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }
}
