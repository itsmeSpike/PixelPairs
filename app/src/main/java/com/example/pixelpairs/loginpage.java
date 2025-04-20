package com.example.pixelpairs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class loginpage extends AppCompatActivity {

    private EditText loginUsername, loginPassword;
    private Button loginContinueButton, loginBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);
        loginContinueButton = findViewById(R.id.logincontinuebutton);
        loginBackButton = findViewById(R.id.loginbackbutton);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        loginContinueButton.setOnClickListener(v -> {
            String enteredUsername = loginUsername.getText().toString().trim();
            String enteredPassword = loginPassword.getText().toString().trim();

            // ✅ Check if the username exists in SharedPreferences
            if (prefs.contains(enteredUsername)) {
                String storedPassword = prefs.getString(enteredUsername, "");

                if (enteredPassword.equals(storedPassword)) {
                    // ✅ Successful login
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isGuest", false); // Optional: track login type
                    editor.putString("currentUser", enteredUsername); // Save current user
                    editor.apply();

                    Intent nextIntent = new Intent(loginpage.this, menupage.class);
                    startActivity(nextIntent);
                    finish();
                } else {
                    Toast.makeText(loginpage.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(loginpage.this, "Username not found", Toast.LENGTH_SHORT).show();
            }
        });

        loginBackButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(loginpage.this, startuppage.class);
            startActivity(backIntent);
            finish();
        });
    }
}
