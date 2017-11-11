package dk.blackdarkness.gg.api.service;

/**
 * Created by awo on 11/11/2017.
 */

public class Highscore {
    private int id;
    private String name;
    private String word;
    private String guessed_letters;
    private String score;

    public Highscore() { }

    public Highscore(int id, String name, String word, String guessed_letters, String score) {
        this.id = id;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
