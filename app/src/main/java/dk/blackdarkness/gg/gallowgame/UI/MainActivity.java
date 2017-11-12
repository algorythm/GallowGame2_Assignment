package dk.blackdarkness.gg.gallowgame.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import dk.blackdarkness.gg.R;
import dk.blackdarkness.gg.api.service.Highscore;
import dk.blackdarkness.gg.gallowgame.ctrl.GameStateManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnStartGame, btnHighscores, btnClearAllPrefs;
    private TextView winnerText, highscoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnStartGame = findViewById(R.id.main_btnStartGame);
        this.btnHighscores = findViewById(R.id.main_btnHighscores);
        this.btnClearAllPrefs = findViewById(R.id.main_clearPrefs);

        this.winnerText = findViewById(R.id.main_winnerText);
        this.highscoreText = findViewById(R.id.main_tvHighscore);

        this.btnStartGame.setOnClickListener(this);
        this.btnHighscores.setOnClickListener(this);
        this.btnClearAllPrefs.setOnClickListener(this);

        this.winnerText.setText("");
        this.setHighscoreText();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.main_btnStartGame: startGame(); break;
            case R.id.main_btnHighscores: gotoHighscores(); break;
            case R.id.main_clearPrefs: clearAllPrefsBtn(); break;
        }
    }
    private void setHighscoreText() {
        double personalHighscore = GameStateManager.getInstance(this).getPersonalHighscore();
        if (personalHighscore == -1.0) {
            this.highscoreText.setText("Personal Highscore: There are no personal highscores.");
        } else {
            this.highscoreText.setText("Personal Highscore: " + personalHighscore);
        }
    }

    private void startGame() {
        final Intent getGallowGameIntent = new Intent(this, GameActivity.class);
        final int result = 1;

        startActivityForResult(getGallowGameIntent, result);
    }

    private void gotoHighscores() {
        final Intent getHighscores = new Intent(this, HighscoresActivity.class);
        startActivity(getHighscores);
    }

    private void clearAllPrefsBtn() {
        GameStateManager.getInstance(this).clearAll();
        this.setHighscoreText();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try
        {
            final boolean gameWon = data.getStringExtra("gameWon").equals("true");
            String gameWonText = "Game was ";

            if (gameWon) gameWonText += "won";
            else         gameWonText += "lost";

            gameWonText += ".";

            this.winnerText.setText(gameWonText);
            this.setHighscoreText();

            // Show save highscore:
            final Intent getAddHSIntent = new Intent(this, AddHighscoreActivity.class);
            startActivity(getAddHSIntent);
        }
        catch (NullPointerException e)
        {
            this.winnerText.setText("Game discontinued - game progress saved.");
        }

        setHighscoreText();
    }
}
