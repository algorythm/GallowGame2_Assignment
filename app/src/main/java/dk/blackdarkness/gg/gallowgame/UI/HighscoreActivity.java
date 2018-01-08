package dk.blackdarkness.gg.gallowgame.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import dk.blackdarkness.gg.R;
import dk.blackdarkness.gg.api.service.Highscore;
import dk.blackdarkness.gg.gallowgame.ctrl.HighscoreService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by awo on 08-01-2018.
 */

public class HighscoreActivity extends AppCompatActivity {
    private TextView id, idDesc;
    private TextView name, nameDesc;
    private TextView word, wordDesc;
    private TextView guessedLetters, guessedLettersDesc;
    private TextView score, scoreDesc;

    private TextView spinnerText;
    private ProgressBar spinner;

    private Highscore currentHighscore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_highscore);

        this.id = findViewById(R.id.hsId_val); this.idDesc = findViewById(R.id.hsId_desc);
        this.name = findViewById(R.id.hsName_val); this.nameDesc = findViewById(R.id.hsName_desc);
        this.word = findViewById(R.id.hsWord_val); this.wordDesc = findViewById(R.id.hsWord_desc);
        this.guessedLetters = findViewById(R.id.hsGuessedLetters_val); this.guessedLettersDesc = findViewById(R.id.hsGuessedLetters_desc);
        this.score = findViewById(R.id.hsScore_val); this.scoreDesc = findViewById(R.id.hsScore_desc);

        this.spinnerText = findViewById(R.id.hs_loadingText);
        this.spinner = findViewById(R.id.hs_loadingSpinner);

        this.currentHighscore = getCurrentHighscore(getIntent());
        fillData(this.currentHighscore);

//        TODO: Temporarily disabled...
//        this.showTextViews();
//        this.fetchHighscore(this.currentHighscore.getId());
    }

    private static Highscore getCurrentHighscore(Intent extras) {
        final int id = extras.getIntExtra("id", -1);
        final String name = extras.getStringExtra("name");
        final String word = extras.getStringExtra("word");
        final String guessedLetters = extras.getStringExtra("guessed_letters");
        final double score = extras.getDoubleExtra("score", -1.0);

        return new Highscore(id, name, word, guessedLetters, score);
    }

    private void fetchHighscore(int id) {
        this.hideTextViews();

        HighscoreService.getFetchAsync(id).enqueue(new Callback<Highscore>() {
            @Override
            public void onResponse(Call<Highscore> call, Response<Highscore> response) {
                Highscore highscore = response.body();

                fillData(highscore);
            }

            @Override
            public void onFailure(Call<Highscore> call, Throwable t) {
                Log.e("FETCH", "Failed to getFetchAsync highscores.");
                Toast failureToast = Toast.makeText(getApplicationContext(), "Failed to fetch highscores. Check internet...", Toast.LENGTH_LONG);
                failureToast.show();
                finish();
            }
        });
    }

    private void fillData(Highscore highscore) {
        this.id.setText(Integer.toString(highscore.getId()));
        this.name.setText(highscore.getName());
        this.word.setText(highscore.getWord());
        this.guessedLetters.setText(highscore.getGuessed_letters());
        this.score.setText(Double.toString(highscore.getScore()));

        this.showTextViews();
    }

    private void showTextViews() {
        this.spinner.setVisibility(View.GONE);
        this.spinnerText.setVisibility(View.GONE);

        this.id.setVisibility(View.VISIBLE);
        this.idDesc.setVisibility(View.VISIBLE);

        this.name.setVisibility(View.VISIBLE);
        this.nameDesc.setVisibility(View.VISIBLE);

        this.word.setVisibility(View.VISIBLE);
        this.wordDesc.setVisibility(View.VISIBLE);

        this.guessedLetters.setVisibility(View.VISIBLE);
        this.guessedLettersDesc.setVisibility(View.VISIBLE);

        this.score.setVisibility(View.VISIBLE);
        this.scoreDesc.setVisibility(View.VISIBLE);
    }

    private void hideTextViews() {
        this.spinner.setVisibility(View.VISIBLE);
        this.spinnerText.setVisibility(View.VISIBLE);

        this.id.setVisibility(View.GONE);
        this.idDesc.setVisibility(View.GONE);

        this.name.setVisibility(View.GONE);
        this.nameDesc.setVisibility(View.GONE);

        this.word.setVisibility(View.GONE);
        this.wordDesc.setVisibility(View.GONE);

        this.guessedLetters.setVisibility(View.GONE);
        this.guessedLettersDesc.setVisibility(View.GONE);

        this.score.setVisibility(View.GONE);
        this.scoreDesc.setVisibility(View.GONE);
    }
}
