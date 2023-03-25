package my.edu.utar.individualassignment7;

public class Score implements Comparable<Score> {
    private String mName;
    private int mScore;

    public Score(String name, int score) {
        mName = name;
        mScore = score;
    }

    public String getName() {
        return mName;
    }

    public int getScore() {
        return mScore;
    }

    @Override
    public int compareTo(Score other) {
        return Integer.compare(mScore, other.mScore);
    }
}
