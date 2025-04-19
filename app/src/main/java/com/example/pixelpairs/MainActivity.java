package com.example.pixelpairs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView playingAsText;

    private final ImageView[] cards = new ImageView[12];
    private final Integer[] cardImages = {
            R.drawable.pacman1, R.drawable.pacman2, R.drawable.pacman3,
            R.drawable.pacman4, R.drawable.pacman5, R.drawable.pacman6
    };

    private final ArrayList<Integer> shuffledImages = new ArrayList<>();
    private int flipCount = 0;
    private CountDownTimer countDownTimer;
    private TextView timerText, flipCounterText, accuracyText;
    private int timerDuration;
    private boolean gameFinished = false;

    private ImageView firstFlippedCard = null;
    private boolean isBusy = false;
    private int correctMatches = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playingAsText = findViewById(R.id.eplayingastext);  // make sure this ID matches the XML

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

        timerText = findViewById(R.id.timertext);
        flipCounterText = findViewById(R.id.flipcounter);
        accuracyText = findViewById(R.id.accuracyText);
        Button homebutton = findViewById(R.id.ehomebutton);

        accuracyText.setText("Accuracy: 0%");

        homebutton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, startuppage.class);
            startActivity(intent);
        });

        String selectedTimer = getIntent().getStringExtra("selectedTimer");
        assert selectedTimer != null;
        setTimerDuration(selectedTimer);

        for (int i = 0; i < 12; i++) {
            @SuppressLint("DiscouragedApi")
            int cardId = getResources().getIdentifier("card" + (i + 1), "id", getPackageName());
            cards[i] = findViewById(cardId);
            cards[i].setTag(i);
            cards[i].setOnClickListener(this::onCardClicked);
        }

        setupGame();
        startTimer();
        Toast.makeText(this, "Received timer: " + selectedTimer, Toast.LENGTH_SHORT).show();
    }

    private void setTimerDuration(String selectedTimer) {
        switch (selectedTimer) {
            case "30 Seconds":
                timerDuration = 30;
                break;
            case "1 Minute":
                timerDuration = 60;
                break;
            case "2 Minutes":
                timerDuration = 120;
                break;
            default:
                timerDuration = 120;
        }
    }

    private void setupGame() {
        shuffledImages.clear();
        for (Integer image : cardImages) {
            shuffledImages.add(image);
            shuffledImages.add(image);
        }
        Collections.shuffle(shuffledImages);

        for (int i = 0; i < 12; i++) {
            cards[i].setImageResource(R.drawable.unflipped);
            cards[i].setTag(shuffledImages.get(i));
        }
    }

    @SuppressLint("SetTextI18n")
    private void onCardClicked(View view) {
        if (isBusy || gameFinished) return;

        ImageView clickedCard = (ImageView) view;

        if (clickedCard.getDrawable().getConstantState() !=
                ContextCompat.getDrawable(this, R.drawable.unflipped).getConstantState()) {
            return;
        }

        int imageResId = (int) clickedCard.getTag();
        clickedCard.setImageResource(imageResId);

        flipCount++;
        flipCounterText.setText(String.valueOf(flipCount));

        if (firstFlippedCard == null) {
            firstFlippedCard = clickedCard;
        } else {
            isBusy = true;
            ImageView secondFlippedCard = clickedCard;

            if (Objects.equals(firstFlippedCard.getTag(), secondFlippedCard.getTag())) {
                correctMatches++;
                updateAccuracy();
                firstFlippedCard = null;
                isBusy = false;

                if (correctMatches == 6) {
                    gameFinished = true;
                    Log.d("GameDebug", "Victory triggered, cancelling timer");

                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }

                    double accuracy = (correctMatches * 2.0 / flipCount) * 100.0;
                    int score = (int) Math.round(accuracy);
                    String selectedTimer = getIntent().getStringExtra("selectedTimer");

                    // Get username
                    SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    String username = userPrefs.getString("username", "Guest");

                    // Save score
                    SharedPreferences prefs = getSharedPreferences("PixelPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    int scoreCount = prefs.getInt("score_count", 0);
                    editor.putString("username_" + scoreCount, username);
                    editor.putInt("score_" + scoreCount, score);
                    editor.putString("difficulty_" + scoreCount, "Easy");
                    editor.putInt("score_count", scoreCount + 1);
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, victorypopup.class);
                    intent.putExtra("accuracy", accuracy);
                    intent.putExtra("selectedDifficulty", "Easy");
                    intent.putExtra("selectedTimer", selectedTimer);
                    startActivity(intent);
                    finish();
                }



            } else {
                secondFlippedCard.postDelayed(() -> {
                    firstFlippedCard.setImageResource(R.drawable.unflipped);
                    secondFlippedCard.setImageResource(R.drawable.unflipped);
                    firstFlippedCard = null;
                    isBusy = false;
                    updateAccuracy();
                }, 1000);
            }
        }
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void updateAccuracy() {
        if (flipCount > 0) {
            double accuracy = (correctMatches * 2.0 / flipCount) * 100;
            accuracyText.setText(String.format("Accuracy:\n%.2f%%", accuracy));
        }
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void startTimer() {
        timerText.setText(String.format("%02d:%02d", timerDuration / 60, timerDuration % 60));

        countDownTimer = new CountDownTimer(timerDuration * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                timerText.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
            }

            @Override
            public void onFinish() {
                Log.d("GameDebug", "onFinish() called, gameFinished = " + gameFinished);

                if (!gameFinished && !isFinishing()) {
                    Toast.makeText(MainActivity.this, "Time's Up!", Toast.LENGTH_SHORT).show();

                    double accuracy = 0.0;
                    if (flipCount > 0) {
                        accuracy = (correctMatches * 2.0 / flipCount) * 100.0;
                    }

                    String selectedTimer = getIntent().getStringExtra("selectedTimer");

                    Intent intent = new Intent(MainActivity.this, timesuppopup.class);
                    intent.putExtra("accuracy", accuracy);
                    intent.putExtra("selectedDifficulty", "Easy");
                    intent.putExtra("selectedTimer", selectedTimer);
                    startActivity(intent);
                    finish();
                }
            }

        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
