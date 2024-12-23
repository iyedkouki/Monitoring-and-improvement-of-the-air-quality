package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView pressuretext,temperaturetext,humiditytext,co2text,setting;
    ImageView pressureimg,co2image,temperatureimage,humidimage,methaneimage;
    TextView name;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db= FirebaseFirestore.getInstance();
        humiditytext=findViewById(R.id.humiditytext);
        pressuretext=findViewById(R.id.pressuretext);
        temperaturetext=findViewById(R.id.temperaturetext);
        co2text=findViewById(R.id.co2text);
        setting=findViewById(R.id.textsetting);

        co2image=findViewById(R.id.imageCo2);
        methaneimage=findViewById(R.id.imagemrthane);
        humidimage=findViewById(R.id.imagehumidity);
        temperatureimage=findViewById(R.id.imagetemperature);
        pressureimg=findViewById(R.id.imagepressure);


        DocumentReference documentReference = db.collection("theSensor").document("Real_Time");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    humiditytext.setText(value.getString("Humidity")+"%");
                }
            }
        });
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    pressuretext.setText(value.getString("Pressure")+"mbar");
                }
            }
        });
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    temperaturetext.setText(value.getString("Temperature")+"°C");
                }
            }
        });

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    co2text.setText(value.getString("CO2")+"ppm");
                }
            }
        });


        pressuretext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Pressure_main.class);
                startActivity(intent);
                finish();

            }
        });
        pressureimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Pressure_main.class);
                startActivity(intent);
                finish();

            }
        });

        humiditytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Humidity_main.class);
                startActivity(intent);
                finish();

            }
        });
        humidimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Humidity_main.class);
                startActivity(intent);
                finish();

            }
        });
        temperaturetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Temperature_main.class);
                startActivity(intent);
                finish();

            }
        });
        temperatureimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Temperature_main.class);
                startActivity(intent);
                finish();

            }
        });
        co2image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, CO2main.class);
                startActivity(intent);
                finish();

            }

        });
        co2text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, CO2main.class);
                startActivity(intent);
                finish();

            }

        });

        methaneimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AllclassYear.class);
                startActivity(intent);
                finish();

            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
                finish();

            }
        });




        name=findViewById(R.id.name);





        mAuth=FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Fetch and display user's name
        String uid = currentUser.getUid();
        DocumentReference userDocRef = db.collection("users").document(currentUser.getEmail());
        userDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    name.setText(", "+value.getString("username"));
                }
            }
        });
        if (currentUser != null) {
            Intent serviceIntent = new Intent(this, TemperatureService.class);
            startService(serviceIntent);

            /*
            // User is logged in, enqueue the periodic worker
            WorkRequest temperatureWorkRequest = new PeriodicWorkRequest.Builder(TemperatureWorker.class, 15, TimeUnit.MINUTES).build();
            WorkManager.getInstance(this).enqueue(temperatureWorkRequest);*/
        }


    }



}