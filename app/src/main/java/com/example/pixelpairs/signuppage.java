package com.example.pixelpairs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signuppage extends AppCompatActivity {

    private EditText signUpUsername, signUpPassword;
    private Button signUpContinueButton;
    private Button signupBakcButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);

        signUpUsername = findViewById(R.id.signupUsername);
        signUpPassword = findViewById(R.id.signupPassword);
        signUpContinueButton = findViewById(R.id.signupcontinubutton);
        signupBakcButton = findViewById(R.id.signupbackbutton);

        signupBakcButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(signuppage.this, startuppage.class); // Replace with your real startup activity
            startActivity(backIntent);
            finish();
        });

        signUpContinueButton.setOnClickListener(v -> {
            String username = signUpUsername.getText().toString().trim();
            String password = signUpPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(signuppage.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                // Save to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("username", username);
                editor.putString("password", password);
                editor.apply();

                // Go to login page
                Intent intent = new Intent(signuppage.this, loginpage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
