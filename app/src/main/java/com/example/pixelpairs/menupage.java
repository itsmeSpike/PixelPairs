package com.example.pixelpairs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class menupage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menupage);

        // Handling the window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button homeButton = findViewById(R.id.menuhomebutton);
        Button playButton = findViewById(R.id.menuplaybutton);
        Button exitButton = findViewById(R.id.menuexitbutton);
        Button leaderboardsButton = findViewById(R.id.leaderboardsbutton);

        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(menupage.this, startuppage.class);
            startActivity(intent);
        });

        playButton.setOnClickListener(view -> {
            Intent intent = new Intent(menupage.this, difficultypage.class);
            startActivity(intent);
        });

        exitButton.setOnClickListener(v -> {
            // Finish the current activity and remove it from the activity stack
            finishAffinity();  // Closes all activities and exits the app
        });

        leaderboardsButton.setOnClickListener(view -> {
            Intent intent = new Intent(menupage.this, leaderboards.class);
            startActivity(intent);
        });

    }
}
