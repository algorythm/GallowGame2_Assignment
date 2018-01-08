package dk.blackdarkness.gg.gallowgame.UI;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dk.blackdarkness.gg.R;
import dk.blackdarkness.gg.gallowgame.ctrl.GameStateManager;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnStartGame, btnHighscores, btnClearAllPrefs, btnCredits;
    private TextView winnerText, highscoreText;

    private KonfettiView konfettiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnStartGame = findViewById(R.id.main_btnStartGame);
        this.btnHighscores = findViewById(R.id.main_btnHighscores);
        this.btnClearAllPrefs = findViewById(R.id.main_clearPrefs);
        this.btnCredits = findViewById(R.id.main_creditsBtn);

        this.winnerText = findViewById(R.id.main_winnerText);
        this.highscoreText = findViewById(R.id.main_tvHighscore);

        this.konfettiView = findViewById(R.id.konfettiView);

        this.btnStartGame.setOnClickListener(this);
        this.btnHighscores.setOnClickListener(this);
        this.btnClearAllPrefs.setOnClickListener(this);
        this.btnCredits.setOnClickListener(this);

        this.winnerText.setText("");
        this.setHighscoreText();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.main_btnStartGame: startGame(); break;
            case R.id.main_btnHighscores: gotoHighscores(); break;
            case R.id.main_clearPrefs: clearAllPrefsBtn(); break;
            case R.id.main_creditsBtn: creditsBtnClicked(); break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.enableButtons();
    }

    private void disableButtons() {
        this.btnStartGame.setEnabled(false);
        this.btnCredits.setEnabled(false);
        this.btnHighscores.setEnabled(false);
    }

    private void enableButtons() {
        this.btnStartGame.setEnabled(true);
        this.btnCredits.setEnabled(true);
        this.btnHighscores.setEnabled(true);
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

        getGallowGameIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivityForResult(getGallowGameIntent, result);
    }

    private void gotoHighscores() {
        final Intent getHighscores = new Intent(this, HighscoresActivity.class);

        getHighscores.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(getHighscores);
    }

    private void clearAllPrefsBtn() {
        GameStateManager.getInstance(this).clearAll();
        this.setHighscoreText();
    }

    private void creditsBtnClicked() {
        final Intent getCredits = new Intent(this, CreditsActivity.class);

        getCredits.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(getCredits);
    }

    private void showKonfetti() {
        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(10000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .stream(300, 5000L);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.winner8bit);
        mp.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try
        {
            final boolean gameWon = data.getStringExtra("gameWon").equals("true");
            String gameWonText = "Game was ";

            if (gameWon) {gameWonText += "won"; showKonfetti(); }
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
