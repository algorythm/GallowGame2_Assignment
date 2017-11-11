package dk.blackdarkness.gg.gallowgame.ctrl;


import java.util.List;

import dk.blackdarkness.gg.api.service.Highscore;
import dk.blackdarkness.gg.api.service.HighscoreClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by awo on 11/11/2017.
 */

public class FetchHighscores  {
    public static Call<List<Highscore>> fetchHighscores() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.gg.blackdarkness.dk/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        HighscoreClient highscoreClient = retrofit.create(HighscoreClient.class);
        Call<List<Highscore>> call = highscoreClient.getHighscore();

        return call;
    }
}
