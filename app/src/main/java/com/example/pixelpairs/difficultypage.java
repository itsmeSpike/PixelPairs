package com.example.pixelpairs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class difficultypage extends AppCompatActivity {

    private TextView playingAsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_difficultypage);

        // Handling the window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the back button and set click listener
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(difficultypage.this, menupage.class);
            startActivity(intent);
        });

        playingAsText = findViewById(R.id.playingastext);  // make sure this ID matches the XML

        // Fetch the username from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "Guest");
        boolean isGuest = prefs.getBoolean("isGuest", true);

        // If the user is a guest, display "Guest"
        if (isGuest) {
            playingAsText.setText("Guest");
        } else {
            // Otherwise, display the username
            playingAsText.setText(username);
        }

        // Initialize the difficulty spinner and set on item selected listener
        Spinner difficultySpinner = findViewById(R.id.difficultySpinner);
        difficultySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, android.view.View selectedItemView, int position, long id) {
                String difficulty = parentView.getItemAtPosition(position).toString();
                Toast.makeText(difficultypage.this, "Selected Difficulty: " + difficulty, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {
                // Handle case when nothing is selected
            }
        });

        // Initialize the timer spinner and set on item selected listener
        Spinner timerSpinner = findViewById(R.id.timerSpinner);
        timerSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, android.view.View selectedItemView, int position, long id) {
                String timer = parentView.getItemAtPosition(position).toString();
                Toast.makeText(difficultypage.this, "Selected Timer: " + timer, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {
                // Handle case when nothing is selected
            }
        });

        // Initialize the play button and set click listener
        Button playButton = findViewById(R.id.difplaybutton);
        playButton.setOnClickListener(view -> {
            String selectedDifficulty = difficultySpinner.getSelectedItem().toString();
            String selectedTimer = timerSpinner.getSelectedItem().toString();

            Toast.makeText(difficultypage.this, "Starting with Difficulty: " + selectedDifficulty + " and Timer: " + selectedTimer, Toast.LENGTH_SHORT).show();

            Intent intent = null;
            switch (selectedDifficulty) {
                case "Easy":
                    intent = new Intent(difficultypage.this, MainActivity.class);
                    break;
                case "Medium":
                    intent = new Intent(difficultypage.this, mediumlevel.class);
                    break;
                case "Hard":
                    intent = new Intent(difficultypage.this, hardlevel.class);
                    break;
                default:
                    Toast.makeText(difficultypage.this, "Invalid difficulty selected", Toast.LENGTH_SHORT).show();
                    return;
            }

            intent.putExtra("selectedDifficulty", selectedDifficulty);
            intent.putExtra("selectedTimer", selectedTimer);
            startActivity(intent);
        });
    }
}
