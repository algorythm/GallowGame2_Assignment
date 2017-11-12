package dk.blackdarkness.gg.gallowgame.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dk.blackdarkness.gg.R;
import dk.blackdarkness.gg.api.service.Highscore;
import dk.blackdarkness.gg.gallowgame.ctrl.HighscoreService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HighscoresActivity extends AppCompatActivity {
    private ProgressBar spinner;
    private TextView loadingText;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        // Adding back buttons
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("HighscoreService");

        spinner = findViewById(R.id.highscores_loadingSpinner);
        loadingText = findViewById(R.id.highscores_loadingText);
        listView = findViewById(R.id.highscores_listView);

        fetchHighscores();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            this.finish();

        return super.onOptionsItemSelected(item);
    }

    private void fetchHighscores() {
        HighscoreService.getFetchAsync().enqueue(new Callback<List<Highscore>>() {
            @Override
            public void onResponse(Call<List<Highscore>> call, Response<List<Highscore>> response) {
                List<Highscore> highscores = response.body();

                fillData(highscores);
            }

            @Override
            public void onFailure(Call<List<Highscore>> call, Throwable t) {
                Log.e("FETCH", "Failed to getFetchAsync highscores.");
                Toast failureToast = Toast.makeText(getApplicationContext(), "Failed to fetch highscores. Check internet...", Toast.LENGTH_LONG);
                failureToast.show();
                finish();
            }
        });
    }

    private void fillData(List<Highscore> highscores) {
        spinner.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);

        for (Highscore h : highscores) {
            Log.d("HIGHSCORE", h.getName() + " : " + h.getScore());
        }

        // Configure listview
        ListAdapter adapter = new HighscoreAdapter(this, highscores);
        listView.setAdapter(adapter);
    }
}
