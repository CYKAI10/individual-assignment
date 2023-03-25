package my.edu.utar.individualassignment7;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private List<View> views;
    private int currentLevel;
    private int score;
    private Handler handler;
    private int GAME_DURATION = 5000;
    private int MAX_LEVEL = 5;
    private int BASE_NUM_VIEWS = 4;
    private int MAX_NUM_VIEWS = MAX_LEVEL * MAX_LEVEL;
    private boolean isGameRunning = false;

    private static final String HIGH_SCORES_KEY = "high_scores";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        views = new ArrayList<>();
        currentLevel = 1;
        score = 0;
        handler = new Handler();
        populateViews(BASE_NUM_VIEWS); // start with 4 views in level 1
        startGame();
    }


    private void populateViews(int numViews) {
        LinearLayout container = findViewById(R.id.container);
        TextView scoreTextView = findViewById(R.id.score_text_view);
        container.removeAllViews();
        views.clear();
        for (int i = 0; i < numViews; i++) {
            HighlightView view = new HighlightView(this);
            view.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (view.isHighlighted) {
                        score++;
                        scoreTextView.setText("Score " + score);
                        view.unhighlight();
                        highlightRandomView();
                    }
                }
            });
            views.add(view);
            container.addView(view);
        }
    }

    private void startGame() {
        TextView levelTextView = findViewById(R.id.level_text_view);
        levelTextView.setText("Level " + currentLevel);
        isGameRunning = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                endGame();
            }
        }, GAME_DURATION); // game lasts 5 seconds
        highlightRandomView();
    }

    private void highlightRandomView() {
        int size = views.size();
        int randomIndex = (int) (Math.random() * size);
        HighlightView randomView = (HighlightView) views.get(randomIndex);
        randomView.highlight();
    }

    private void endGame() {
        isGameRunning = false;
        currentLevel++;
        if (currentLevel <= MAX_LEVEL) {
            int numViews = currentLevel * currentLevel;
            populateViews(Math.min(numViews, MAX_NUM_VIEWS));
            startGame();
        } else {
            showNameDialog();
        }
    }

//    private void saveScore(String name, int score) {
//        SharedPreferences sharedPreferences = getSharedPreferences("high_scores", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt(name, score);
//        editor.apply();
//    }

    private void saveScore(int score) {
        SharedPreferences sharedPreferences = getSharedPreferences(HIGH_SCORES_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("score", score);
        editor.apply();
    }


    public void quitGame(View view) {
        // Stop the timer
        handler.removeCallbacksAndMessages(null);

        // Show a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit Game");
        builder.setMessage("Leaving so soon? Are you sure you want to quit the game?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close the activity and return to the previous screen
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

//    private void showNameDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("SPECTACULAR!!! New high score! Enter your name:");
//        final EditText nameEditText = new EditText(this);
//        builder.setView(nameEditText);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String name = nameEditText.getText().toString();
//                saveScore(name, score);
//                if (isTop25Score(score)) {
//                    showHighScores();
//                } else {
//                    finish();
//                }
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.show();
//    }

    private void showNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SPECTACULAR!!! New high score! Enter your name:");
        final EditText nameEditText = new EditText(this);
        builder.setView(nameEditText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                saveScore(score);
                if (isTop25Score(score)) {
                    showHighScores();
                } else {
                    finish();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    private boolean isTop25Score(int score) {
        SharedPreferences prefs = getSharedPreferences("scores", MODE_PRIVATE);
        Set<String> scoresSet = prefs.getStringSet("scoresSet", new HashSet<String>());
        List<Integer> scoresList = new ArrayList<>();
        for (String scoreStr : scoresSet) {
            scoresList.add(Integer.parseInt(scoreStr.split(":")[1]));
        }
        scoresList.add(score);
        Collections.sort(scoresList, Collections.reverseOrder());
        if (scoresList.size() > 25) {
            scoresList.remove(25);
        }
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> newScoresSet = new HashSet<>();
        for (int i = 0; i < scoresList.size(); i++) {
            int rank = i + 1;
            int scoreValue = scoresList.get(i);
            String scoreStr = rank + ":" + scoreValue;
            newScoresSet.add(scoreStr);
        }
        editor.putStringSet("scoresSet", newScoresSet);
        editor.apply();
        return scoresList.indexOf(score) < 25;
    }

    private void showHighScores() {
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
        finish();
    }
}