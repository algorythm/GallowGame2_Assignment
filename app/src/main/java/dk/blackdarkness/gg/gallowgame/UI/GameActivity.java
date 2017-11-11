package dk.blackdarkness.gg.gallowgame.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import dk.blackdarkness.gg.R;
import dk.blackdarkness.gg.gallowgame.logic.GallowGame;

/**
 * Created by awo on 11/11/2017.
 */

public class GameActivity extends AppCompatActivity {
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;

    private final String gameProgressPreferenceName = "gameProgress";

    private GallowGame game;
    private TextView tvVisibleWord;
    private TextView tvGuessedLetters;
    private ImageView gallowImage;
    private Button guessBtn;
    private EditText etGuessLetter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallowgame);

        mPrefs = getPreferences(MODE_PRIVATE);
        prefsEditor = mPrefs.edit();

        this.tvVisibleWord = findViewById(R.id.game_tvVisibleWord);
        this.tvGuessedLetters = findViewById(R.id.game_tvGuessedLetters);
        this.gallowImage = findViewById(R.id.game_gallowImage);
        this.guessBtn = findViewById(R.id.game_guessBtn);
        this.etGuessLetter = findViewById(R.id.game_etGuessLetter);

        // Adding back buttons
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set guess button action
        this.guessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guessLetter();
            }
        });

        // Set enter button on edit text
        this.etGuessLetter.setImeActionLabel("Guess!", KeyEvent.KEYCODE_ENTER);

        // Set enter button action
        etGuessLetter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == KeyEvent.KEYCODE_ENTER) {
                    guessLetter();
                    handled = true;
                }
                return handled;
            }
        });

        this.startNewGame();
        this.guessLetter();
    }

    private void startNewGame() {
        loadProgressOrStartNew();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            // Save progress
            saveProgress();

            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveProgress() {
        Gson gson = new Gson();
        String json =gson.toJson(game);

        prefsEditor.putString(gameProgressPreferenceName, json);
        prefsEditor.commit();
    }

    private void loadProgressOrStartNew() {
        // Try to load a new game
        Gson gson = new Gson();
        String json = mPrefs.getString(gameProgressPreferenceName, null);

        GallowGame oldGame = gson.fromJson(json, GallowGame.class);
        this.game = oldGame;

        // If no previous game, start a new
        if (this.game == null) {
            this.game = new GallowGame();
        }

        setGallowImage();
        this.tvVisibleWord.setText(this.game.getVisibleWord());
    }

    private void clearProgress() {
        prefsEditor.remove(gameProgressPreferenceName).commit();
    }

    private void guessLetter() {
        checkGameOver();

        Log.d("GUESS", this.etGuessLetter.getText().toString());
        game.guess(etGuessLetter.getText().toString()); // only first letter


        // Reset edit text input
        etGuessLetter.setText("");


        setGallowImage();
        tvVisibleWord.setText(game.getVisibleWord());
        tvGuessedLetters.setText(game.getUsedLetters().toString());

        game.logStatus();

        checkGameOver();

    }

    private void setGallowImage() {
        if (game.getWrongLettersCount() > 0) {
            switch (game.getWrongLettersCount()) {
                case 1: gallowImage.setImageDrawable(getDrawable(R.drawable.wrong1)); break;
                case 2: gallowImage.setImageDrawable(getDrawable(R.drawable.wrong2)); break;
                case 3: gallowImage.setImageDrawable(getDrawable(R.drawable.wrong3)); break;
                case 4: gallowImage.setImageDrawable(getDrawable(R.drawable.wrong4)); break;
                case 5: gallowImage.setImageDrawable(getDrawable(R.drawable.wrong5)); break;
                case 6: gallowImage.setImageDrawable(getDrawable(R.drawable.wrong6)); break;
                default: gallowImage.setImageDrawable(getDrawable(R.drawable.gallow));
            }
        }
    }

    private void checkGameOver() {
        if (!game.isGameOver()) return;

        final Intent goingBack = new Intent();

        Log.d("GAME_STATUS", "Game over!");

        final String gameWon = game.isGameWon() ? "true" : "false";

        goingBack.putExtra("gameWon", gameWon);
        setResult(RESULT_OK, goingBack);

        clearProgress();

        this.finish();
    }
}
