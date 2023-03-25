package my.edu.utar.individualassignment7;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScoreManager {
    private static final String PREFS_NAME = "scores";
    private static final int MAX_SCORES = 25;
    private static ScoreManager instance;
    private SharedPreferences prefs;

    private ScoreManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static ScoreManager getInstance(Context context) {
        if (instance == null) {
            instance = new ScoreManager(context);
        }
        return instance;
    }

//    public void addScore(String name, int score) {
//        Set<String> scoresSet = prefs.getStringSet("scoresSet", new HashSet<String>());
//        scoresSet.add(name + ":" + score);
//
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putStringSet("scoresSet", scoresSet);
//        editor.apply();
//    }

    public void addScore(String name, int score) {
        Set<Score> scoresSet = getScoreSet();
        scoresSet.add(new Score(name, score));
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("scoresSet", getScoreSetStringSet(scoresSet));
        editor.apply();
    }

    private Set<Score> getScoreSet() {
        Set<String> scoresStringSet = prefs.getStringSet("scoresSet", new HashSet<String>());
        Set<Score> scoresSet = new HashSet<>();
        for (String scoreString : scoresStringSet) {
            String[] parts = scoreString.split(":");
            String name = parts[0];
            int score = Integer.parseInt(parts[1]);
            scoresSet.add(new Score(name, score));
        }
        return scoresSet;
    }

    private Set<String> getScoreSetStringSet(Set<Score> scoresSet) {
        Set<String> scoresStringSet = new HashSet<>();
        for (Score score : scoresSet) {
            String scoreString = score.getName() + ":" + score.getScore();
            scoresStringSet.add(scoreString);
        }
        return scoresStringSet;
    }



    public List<ScoreEntry> getTopScores() {
        Set<String> scoresSet = prefs.getStringSet("scoresSet", new HashSet<String>());
        List<ScoreEntry> scoresList = new ArrayList<>();
        for (String scoreStr : scoresSet) {
            String[] parts = scoreStr.split(":");
            String name = parts[0];
            int score = Integer.parseInt(parts[1]);
            scoresList.add(new ScoreEntry(name, score));
        }
        Collections.sort(scoresList, Collections.reverseOrder());
        if (scoresList.size() > MAX_SCORES) {
            scoresList = scoresList.subList(0, MAX_SCORES);
        }
        return scoresList;
    }

    public void clearScores() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public static class ScoreEntry implements Comparable<ScoreEntry> {
        private final String name;
        private final int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        @Override
        public int compareTo(ScoreEntry other) {
            return Integer.compare(score, other.score);
        }
    }
}