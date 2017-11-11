package dk.blackdarkness.gg.gallowgame.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dk.blackdarkness.gg.R;

public class MainActivity extends AppCompatActivity {
    private Button btnStartGame;
    private Button btnHighscores;
    private TextView demotext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnStartGame = findViewById(R.id.main_btnStartGame);
        this.btnHighscores = findViewById(R.id.main_btnHighscores);

        this.demotext = findViewById(R.id.demotext);

        this.btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });
        this.btnHighscores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoHighscores();
            }
        });

        this.demotext.setText("");
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

            this.demotext.setText(gameWonText);
        }
        catch (NullPointerException e)
        {
            this.demotext.setText("Game discontinued - game progress saved.");
        }
    }
}
