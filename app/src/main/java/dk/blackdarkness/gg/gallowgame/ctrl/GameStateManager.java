package dk.blackdarkness.gg.gallowgame.ctrl;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Arrays;

import dk.blackdarkness.gg.R;
import dk.blackdarkness.gg.api.service.Highscore;
import dk.blackdarkness.gg.gallowgame.logic.GallowGame;

import static android.content.Context.MODE_PRIVATE;

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
        mPrefs = this.act.getPreferences(MODE_PRIVATE);
        prefsEditor = mPrefs.edit();

        this.gameProgressPreferenceName = act.getResources().getString(R.string.gameProgressPreferenceName);
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

    public GallowGame loadProgressOrStartNew() {
        // Try to load a new game
        GallowGame game;

        Gson gson = new Gson();
        String json = mPrefs.getString(gameProgressPreferenceName, null);

        if (json == null) System.out.println("JSON IS NULL");

        GallowGame oldGame = gson.fromJson(json, GallowGame.class);
        game = oldGame;

        // If no previous game, start a new
        if (game == null) {
            System.out.println("GAME IS NULL");
            game = new GallowGame();
        }

        return game;
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

        final Highscore highscore = new Highscore(null, game.getWord(), game.getUsedLettersStr(), game.calculateScoreStr());

        String json = gson.toJson(highscore);

        if (game.calculateScore() > getPersonalHighscore())
            savePersonalHighscore(json);

        saveLatestScore(json);
    }

    private void saveLatestScore(String json) {
        prefsEditor.putString(latestScorePreference, json);
        prefsEditor.commit();
    }

    private void savePersonalHighscore(String json) {
        prefsEditor.putString(highestScorePreference, json);
        prefsEditor.commit();
    }

    public double getLatestScore() {
        Gson gson = new Gson();
        String json = mPrefs.getString(latestScorePreference, null);

        final Highscore highscoreObj = gson.fromJson(json, Highscore.class);

        if (highscoreObj == null) return -1.0;

        double highscore = -1.0;

        try {
            highscore = Double.parseDouble(highscoreObj.getScore());
        } catch (NumberFormatException e) {
            Log.e("PARSE_FAIL", "Failed to parse stored score as a double.", e);
        }

        return highscore;
    }

    public double getPersonalHighscore() {
        Gson gson = new Gson();
        String json = mPrefs.getString(highestScorePreference, null);

        final Highscore highscoreObj = gson.fromJson(json, Highscore.class);

        if (highscoreObj == null) return -1.0;

        double highscore = -1.0;

        try {
            highscore = Double.parseDouble(highscoreObj.getScore());
        } catch (NumberFormatException e) {
            Log.e("PARSE_FAIL", "Failed to parse stored score as a double.", e);
        }

        return highscore;
    }
}
