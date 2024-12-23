package com.example.myapplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;


    EditText email, password;
    TextView createAccount,forgPass;
    Button login;
    ImageView google;
    FirebaseFirestore db;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        createAccount = findViewById(R.id.register);
        forgPass= findViewById(R.id.forgot_password);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        login.setOnClickListener(view -> checkDataEntered());
        createAccount.setOnClickListener(view -> startActivity(new Intent(login.this, register.class)));
        forgPass.setOnClickListener(view -> startActivity(new Intent(login.this, Passwordreset.class)));
        // Declare the GoogleSignInClient at the top of your activity or fragment



    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void checkDataEntered() {
        String mail = email.getText().toString();
        String pass = password.getText().toString();

        if (isEmpty(email) || isEmpty(password)) {
            Toast.makeText(this, "Username and password are required!", Toast.LENGTH_SHORT).show();
            return; // Exit the method
        }

        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        startChoseActivity();
                    } else {
                        Toast.makeText(login.this, "Invalid Email or password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void startChoseActivity() {
        startActivity(new Intent(login.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if the user is signed in (non-null) and update the UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startChoseActivity();
        }
    }



    // Implement the handleGoogleSignInResult method
    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            showToast("Google Sign In Failed: " + e.getStatusCode());
        }
    }

    // Implement the firebaseAuthWithGoogle method
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUserDataToFirestore(user.getEmail(), user.getDisplayName());
                            onRegistrationSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            showToast("Google Authentication Failed.");
                        }
                    }
                });
    }


    // Implement the updateUI method (customize it based on your requirements)
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is signed in, navigate to the main activity or perform other actions
            showToast("Google Sign In Successful!");
            startActivity(new Intent(login.this, MainActivity.class));
            finish();
        } else {
            // User is signed out or authentication failed
            showToast("Google Sign In Failed.");
        }
    }

    // Helper method to show Toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void saveUserDataToFirestore(String email, String username) {
        // Check if the document with the email already exists
        db.collection("users")
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Document with the email already exists, show a message or take appropriate action
                                showToast("User all ready registered");
                            } else {
                                // Document with the email doesn't exist, proceed to save user data
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("email", email);
                                userData.put("username", username);

                                db.collection("users")
                                        .document(email)
                                        .set(userData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    showToast("User data saved to Firestore");
                                                } else {
                                                    showToast("Error saving user data to Firestore");
                                                }
                                            }
                                        });
                            }
                        } else {
                            showToast("Error checking user existence in Firestore");
                        }
                    }
                });
    } private void onRegistrationSuccess() {
        showToast("Registration successful!");

        // Proceed to the main activity or any other logic
        startActivity(new Intent(login.this, MainActivity.class));
    }



}