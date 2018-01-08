package dk.blackdarkness.gg.gallowgame.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private List<Highscore> highscores;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listItemClicked(i);
            }
        });

        fetchHighscores();
    }

    private void listItemClicked(int index) {
        Highscore highscore = this.highscores.get(index);

        Intent getHighscore = new Intent(this, HighscoreActivity.class);

        Bundle b = new Bundle();
        b.putInt("id", highscore.getId());
        b.putString("name", highscore.getName());
        b.putString("word", highscore.getWord());
        b.putString("guessed_letters", highscore.getGuessed_letters());
        b.putDouble("score", highscore.getScore());
        getHighscore.putExtras(b);

        startActivity(getHighscore);
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
                List<Highscore> fetchedHighscores = response.body();

                highscores = fetchedHighscores;
                fillData(fetchedHighscores);
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
