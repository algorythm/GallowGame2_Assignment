package dk.blackdarkness.gg.gallowgame.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import java.io.IOException;

import dk.blackdarkness.gg.R;
import dk.blackdarkness.gg.gallowgame.ctrl.GameStateManager;
import dk.blackdarkness.gg.gallowgame.logic.GallowGame;

/**
 * Created by awo on 11/11/2017.
 */

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private GallowGame game;
    private TextView tvVisibleWord;
    private TextView tvGuessedLetters;
    private ImageView gallowImage;
    private Button guessBtn, newWordBtn;
    private EditText etGuessLetter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallowgame);

        this.tvVisibleWord = findViewById(R.id.game_tvVisibleWord);
        this.tvGuessedLetters = findViewById(R.id.game_tvGuessedLetters);
        this.gallowImage = findViewById(R.id.game_gallowImage);
        this.guessBtn = findViewById(R.id.game_guessBtn);
        this.newWordBtn = findViewById(R.id.game_newWordBtn);
        this.etGuessLetter = findViewById(R.id.game_etGuessLetter);

        guessBtn.setOnClickListener(this);
        newWordBtn.setOnClickListener(this);

        // Adding back buttons
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        initializeGame();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            // Save progress
            GameStateManager.getInstance(this).saveProgress(this.game);

            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.game_guessBtn: guessLetter(); break;
            case R.id.game_newWordBtn: newWordBtnClicked(); break;
        }
    }

    private void initializeGame() {
        final GallowGame oldGame = GameStateManager.getInstance(this).loadOldGameProgress();

        if (oldGame == null) {
            this.game = new GallowGame();
            this.game.reset();
        } else {
            this.game = oldGame;
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    game.getWordsFromWeb("https://www.reddit.com/r/ProgrammerDadJokes/");
                } catch (IOException e) {
                    Log.e("Failed to fetcg", "Blah!!!! " + e.getMessage(), e);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                tvVisibleWord.setText("Fetching from web...");
                guessBtn.setEnabled(false);
                etGuessLetter.setEnabled(false);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                guessBtn.setEnabled(true);
                etGuessLetter.setEnabled(true);

                setGallowImage();
                System.out.println("Word = " + game.getWord());
                tvVisibleWord.setText(game.getVisibleWord());
            }
        }.execute();

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

    private void newWordBtnClicked() {
        game.reset();
        System.out.println("New word = " + game.getWord());
        tvVisibleWord.setText(game.getVisibleWord());
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

        GameStateManager.getInstance(this).clearProgress();
        GameStateManager.getInstance(this).saveScore(this.game);

        this.finish();
    }
}
