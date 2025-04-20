package com.example.pixelpairs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class leaderboards extends AppCompatActivity {

    private TextView lbUser, lbScore;
    private Button lbBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);

        lbUser = findViewById(R.id.lbuser);
        lbScore = findViewById(R.id.lbscore);
        lbBackButton = findViewById(R.id.lbbackbutton);

        SharedPreferences prefs = getSharedPreferences("PixelPrefs", MODE_PRIVATE);
        int scoreCount = prefs.getInt("score_count", 0);

        ArrayList<HashMap<String, Object>> scoreList = new ArrayList<>();

        for (int i = 0; i < scoreCount; i++) {
            String username = prefs.getString("username_" + i, "Unknown");
            int score = prefs.getInt("score_" + i, 0);
            String difficulty = prefs.getString("difficulty_" + i, "Unknown");

            HashMap<String, Object> entry = new HashMap<>();
            entry.put("username", username);
            entry.put("score", score);
            entry.put("difficulty", difficulty);
            scoreList.add(entry);
        }

        // Sort from highest to lowest
        Collections.sort(scoreList, new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                return ((Integer) o2.get("score")) - ((Integer) o1.get("score"));
            }
        });

        // Build two columns
        StringBuilder userColumn = new StringBuilder();
        StringBuilder scoreColumn = new StringBuilder();

        for (HashMap<String, Object> entry : scoreList) {
            String userLine = entry.get("username") + " (" + entry.get("difficulty") + ")";
            String scoreLine = entry.get("score") + "%";

            userColumn.append(userLine).append("\n");
            scoreColumn.append(scoreLine).append("\n");
        }

        lbUser.setText(userColumn.toString());
        lbScore.setText(scoreColumn.toString());

        lbBackButton.setOnClickListener(v -> finish()); // Go back when button pressed

        /*Button clearButton = findViewById(R.id.clearLeaderboardBtn);
        clearButton.setOnClickListener(v -> {

            SharedPreferences.Editor editor = prefs.edit();
            editor.clear(); // or editor.clear() to wipe everything
            editor.apply();

            Toast.makeText(this, "Leaderboard cleared!", Toast.LENGTH_SHORT).show();

            // Optionally, refresh the screen or update the TextViews
            // For example:
            lbUser.setText("");
            lbScore.setText("");
        }); */

    }
}
