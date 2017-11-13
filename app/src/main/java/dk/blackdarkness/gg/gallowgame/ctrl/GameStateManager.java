package dk.blackdarkness.gg.gallowgame.ctrl;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import dk.blackdarkness.gg.R;
import dk.blackdarkness.gg.api.service.Highscore;
import dk.blackdarkness.gg.gallowgame.logic.GallowGame;

/**
 * Created by awo on 12/11/2017.
 */

public final class GameStateManager {
    private static volatile GameStateManager instance;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;

    private Activity act;

    private String gameProgressPreferenceName;
    private String latestScorePreference;
    private String highestScorePreference;

    private GameStateManager(Activity act) {
        this.act = act;

        this.initialize();
    }

    private void initialize() {
        mPrefs = this.act.getPreferences(Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();

        this.gameProgressPreferenceName = act.getResources().getString(R.string.gameProgressPreferenceName);
        this.latestScorePreference = act.getResources().getString(R.string.latestScorePreference);
        this.highestScorePreference = act.getResources().getString(R.string.highestScorePreference);
    }

    public static GameStateManager getInstance(Activity act) {
        if (instance == null) {
            synchronized (GameStateManager.class) {
                if (instance == null) {
                    instance = new GameStateManager(act);
                    System.out.println("CREATED NEW INSTANCE");
                }
            }
        }

        return instance;
    }

    public void saveProgress(GallowGame game) {
        Gson gson = new Gson();
        String json =gson.toJson(game);

        prefsEditor.putString(gameProgressPreferenceName, json);
        prefsEditor.commit();
    }

    public GallowGame loadOldGameProgress() {
        final Gson gson = new Gson();
        final String json = mPrefs.getString(gameProgressPreferenceName, null);
        final GallowGame oldGame = gson.fromJson(json, GallowGame.class);

        return oldGame;
    }

    public void clearProgress() {
        prefsEditor.remove(gameProgressPreferenceName).commit();
    }

    public void clearPersonalHighscore() {
        prefsEditor.remove(highestScorePreference).commit();
    }

    public void clearLatestScore() {
        prefsEditor.remove(latestScorePreference).commit();
    }

    public void clearAll() {
        prefsEditor.clear().commit();
    }

    public void saveScore(GallowGame game) {
        Gson gson = new Gson();
        System.out.println();

        final Highscore score = new Highscore(null, game.getWord(), game.getUsedLettersStr(), game.calculateScore());

        String json = gson.toJson(score);

        if (game.calculateScore() > getPersonalHighscore()) {
            savePersonalHighscore(json);
        }

        saveLatestScore(json);
    }

    public void saveLatestScore(String json) {
        prefsEditor.putString(latestScorePreference, json).commit();
    }

    public void savePersonalHighscore(String json) {
        prefsEditor.putString(highestScorePreference, json).commit();
    }

    public double getLatestScore() {
        final Highscore highscoreObj = getLatestScoreObj();

        if (highscoreObj == null) return -1.0;

        double highscore = -1.0;

        try {
            highscore = highscoreObj.getScore();
        } catch (NumberFormatException e) {
            Log.e("PARSE_FAIL", "Failed to parse stored score as a double.", e);
        }

        return highscore;
    }

    public Highscore getLatestScoreObj() {
        Gson gson = new Gson();
//        String json = mPrefs.getString(latestScorePreference, null);
        String json = mPrefs.getString(latestScorePreference, null);

        final Highscore highscoreObj = gson.fromJson(json, Highscore.class);

        return highscoreObj;
    }

    public double getPersonalHighscore() {
        Gson gson = new Gson();
//        String json = mPrefs.getString(highestScorePreference, null);
        String json = mPrefs.getString(highestScorePreference, null);

        final Highscore highscoreObj = gson.fromJson(json, Highscore.class);

        if (highscoreObj == null) return -1.0;

        double highscore = -1.0;

        try {
            highscore = highscoreObj.getScore();
        } catch (NumberFormatException e) {
            Log.e("PARSE_FAIL", "Failed to parse stored score as a double.", e);
        }

        return highscore;
    }
}
