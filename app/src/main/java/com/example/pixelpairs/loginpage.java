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

        // Retrieve from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUsername = prefs.getString("username", null);
        String savedPassword = prefs.getString("password", null);

        loginContinueButton.setOnClickListener(v -> {
            String enteredUsername = loginUsername.getText().toString().trim();
            String enteredPassword = loginPassword.getText().toString().trim();

            if (enteredUsername.equals(savedUsername) && enteredPassword.equals(savedPassword)) {
                // Save the username and mark the user as not a guest
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("username", enteredUsername); // Save the username
                editor.putBoolean("isGuest", false); // Set guest to false upon successful login
                editor.apply();

                // Proceed to the difficulty page
                Intent nextIntent = new Intent(loginpage.this, menupage.class);
                startActivity(nextIntent);
                finish();
            } else {
                Toast.makeText(loginpage.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
            }
        });

        loginBackButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(loginpage.this, startuppage.class); // Replace with your real startup activity
            startActivity(backIntent);
            finish();
        });
    }
}
