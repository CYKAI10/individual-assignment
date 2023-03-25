package my.edu.utar.individualassignment7;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class HighScoresActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<Score> mScoresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        mPrefs = getSharedPreferences("high_scores", MODE_PRIVATE);
        mListView = findViewById(R.id.high_scores_list);
        mScoresList = new ArrayList<Score>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);

        // Read scores from shared preferences and add them to the list
        Map<String, ?> allEntries = mPrefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            String name;
            int underscoreIndex = key.indexOf("_");
            if (underscoreIndex != -1) {
                name = key.substring(0, underscoreIndex);
            } else {
                name = "Unknown";
            }
            Integer scoreValue = (Integer) entry.getValue();
            Score score = new Score(name, scoreValue);
            mScoresList.add(score);
        }

        // Sort the list in descending order
        Collections.sort(mScoresList, Collections.reverseOrder());

        // Add the top 25 scores to the adapter
        for (int i = 0; i < 25 && i < mScoresList.size(); i++) {
            Score score = mScoresList.get(i);
            //mAdapter.add(score.toString());
            mAdapter.add(score.getName() + " - " + score.getScore());
        }
    }



}