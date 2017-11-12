package dk.blackdarkness.gg.gallowgame.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import dk.blackdarkness.gg.R;
import dk.blackdarkness.gg.api.service.Highscore;
import dk.blackdarkness.gg.gallowgame.ctrl.GameStateManager;
import dk.blackdarkness.gg.gallowgame.ctrl.HighscoreService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by awo on 12/11/2017.
 */

public class AddHighscoreActivity extends AppCompatActivity implements View.OnClickListener {
    private Button saveBtn, cancelBtn;
    private TextView scoreText;
    private EditText nameText;
    private ProgressBar spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_highscore);

        saveBtn = findViewById(R.id.addhs_saveBtn);
        cancelBtn = findViewById(R.id.addhs_cancelBtn);
        scoreText = findViewById(R.id.addhs_score);
        nameText = findViewById(R.id.addhs_name);
        spinner = findViewById(R.id.addhs_spinner);

        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        scoreText.setText(Double.toString(GameStateManager.getInstance(this).getLatestScore()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addhs_saveBtn: saveBtnClicked(); break;
            case R.id.addhs_cancelBtn: cancelBtnClicked(); break;
        }
    }

    private void saveBtnClicked() {
        if (nameText.getText().toString().equals("")) {
            Toast t = Toast.makeText(getApplicationContext(), "No name were given.", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        spinner.setVisibility(View.VISIBLE);

        Toast t = Toast.makeText(getApplicationContext(), "Posting highscore online...", Toast.LENGTH_SHORT);
        t.show();

        Highscore highscore = GameStateManager.getInstance(this).getLatestScoreObj();

        highscore.setName(nameText.getText().toString());
        System.out.println(highscore.getName() + " " + highscore.getScore());

        final Activity tempActivity = this;

        HighscoreService.getPostAsync(highscore).enqueue(new Callback<Highscore>() {
            @Override
            public void onResponse(Call<Highscore> call, Response<Highscore> response) {
                System.out.println("SUCCESS!!! Check highscores");
                GameStateManager.getInstance(tempActivity).clearLatestScore();
                finish();
            }

            @Override
            public void onFailure(Call<Highscore> call, Throwable t) {
                Toast failureToast = Toast.makeText(getApplicationContext(), "Failed to post highscore. Try again...", Toast.LENGTH_LONG);
                failureToast.show();

                saveBtn.setEnabled(true);
                cancelBtn.setEnabled(true);
                spinner.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void cancelBtnClicked() {
        System.out.println("YO, I was clicked!");
        GameStateManager.getInstance(this).clearLatestScore();

        finish();
    }
}
