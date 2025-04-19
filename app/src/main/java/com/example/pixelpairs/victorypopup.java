package com.example.pixelpairs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class victorypopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_victorypopup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button homebutton = findViewById(R.id.victoryhomebutton);
        Button restartbutton = findViewById(R.id.victoryrestartbutton);
        TextView accuracyText = findViewById(R.id.victoryaccuracyText); // Ensure this exists in XML

        // Retrieve data from intent
        Intent intent = getIntent();
        double accuracy = intent.getDoubleExtra("accuracy", 0.0);
        String difficulty = intent.getStringExtra("selectedDifficulty");
        String selectedTimer = intent.getStringExtra("selectedTimer"); // NEW: retrieve timer

        // Show the accuracy
        accuracyText.setText(String.format("Accuracy:\n%.2f%%", accuracy));


        // Home button logic
        homebutton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(victorypopup.this, menupage.class);
            startActivity(homeIntent);
            finish(); // optional to prevent stacking
        });

        // Restart button logic with timer included
        restartbutton.setOnClickListener(v -> {
            Intent restartIntent;

            // Choose activity based on difficulty
            if ("Easy".equals(difficulty)) {
                restartIntent = new Intent(victorypopup.this, MainActivity.class);
            } else if ("Medium".equals(difficulty)) {
                restartIntent = new Intent(victorypopup.this, mediumlevel.class);
            } else if ("Hard".equals(difficulty)) {
                restartIntent = new Intent(victorypopup.this, hardlevel.class);
            } else {
                restartIntent = new Intent(victorypopup.this, startuppage.class); // fallback
            }

            // Pass timer and difficulty again
            restartIntent.putExtra("selectedTimer", selectedTimer);
            restartIntent.putExtra("selectedDifficulty", difficulty);

            startActivity(restartIntent);
            finish(); // optional
        });
    }
}
