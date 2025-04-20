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
            Intent backIntent = new Intent(signuppage.this, startuppage.class);
            startActivity(backIntent);
            finish();
        });

        signUpContinueButton.setOnClickListener(v -> {
            String username = signUpUsername.getText().toString().trim();
            String password = signUpPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(signuppage.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

                // Check if username already exists
                if (prefs.contains(username)) {
                    Toast.makeText(signuppage.this, "Username already taken. Please choose another.", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = prefs.edit();

                    // Save username as key, password as value
                    editor.putString(username, password);
                    editor.apply();

                    Toast.makeText(signuppage.this, "Signup successful!", Toast.LENGTH_SHORT).show();

                    // Proceed to login
                    Intent intent = new Intent(signuppage.this, loginpage.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
