package com.example.myapplication;

public class TemperatureWorker {/*extends Worker {Worker, indicating it's meant to perform background tasks.
    private FirebaseFirestore db;
    private Context context;

    public TemperatureWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public Result doWork() {
        checkTemperature();
        return Result.success();
    }

    private void checkTemperature() {
        DocumentReference documentReference = db.collection("RealSensor").document("Real_Time");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    String tempStr = value.getString("Temperature");
                    if (tempStr != null) {
                        float temperature = Float.parseFloat(tempStr);
                        if (temperature > 34.01) {
                            sendNotification("Temperature exceeded 34 degrees!");
                        }
                    }
                }
            }
        });
    }

    private void sendNotification(String message) {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "temperatureAlert")
                .setSmallIcon(R.drawable.icontemperaturealert)
                .setContentTitle("Temperature Alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }*/
}
