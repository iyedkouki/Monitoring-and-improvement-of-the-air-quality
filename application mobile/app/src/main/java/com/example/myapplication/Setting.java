package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ActivityNotFoundException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Setting extends AppCompatActivity {
    Button button;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView emailTextView,phoneTextView,general;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        db= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        button=findViewById(R.id.logout);//change place
        button.setOnClickListener(new View.OnClickListener(){//change place
            @Override
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(Setting.this, login.class);
                startActivity(intent);
                finish();
            }
        });
        general=findViewById(R.id.textgeneral);
        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Setting.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        phoneTextView = findViewById(R.id.phoneTextView);

        phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the phone dialer
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:+21622018161")); // Replace with the phone number

                // Check if there is an app available to handle the intent
                if (dialIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(dialIntent);
                }
            }
        });
    }


}