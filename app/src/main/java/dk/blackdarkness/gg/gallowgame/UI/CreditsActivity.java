package dk.blackdarkness.gg.gallowgame.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;

import dk.blackdarkness.gg.R;
import dk.blackdarkness.gg.gallowgame.ctrl.GameStateManager;

/**
 * Created by awo on 12/11/2017.
 */

public class CreditsActivity extends AppCompatActivity {
    private WebView webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        // Adding back buttons
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webview = findViewById(R.id.credits_webview);

        webview.loadUrl("https://gg.blackdarkness.dk/credits.html");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
