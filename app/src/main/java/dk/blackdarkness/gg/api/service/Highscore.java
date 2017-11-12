package dk.blackdarkness.gg.api.service;

/**
 * Created by awo on 11/11/2017.
 */

public class Highscore {
    private int id;
    private String name;
    private String word;
    private String guessed_letters;
    private double score;

    public Highscore() { }

    public Highscore(String name, String word, String guessed_letters, double score) {
        this.name = name;
        this.word = word;
        this.guessed_letters = guessed_letters;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getGuessed_letters() {
        return guessed_letters;
    }

    public void setGuessed_letters(String guessed_letters) {
        this.guessed_letters = guessed_letters;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
