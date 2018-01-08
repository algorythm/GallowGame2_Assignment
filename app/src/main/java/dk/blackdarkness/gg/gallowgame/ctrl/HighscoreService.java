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

public class HighscoreService {
    private  static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://highscore-api.wiberg.tech/")
            .addConverterFactory(GsonConverterFactory.create());

    public static Call<List<Highscore>> getFetchAsync() {
        Retrofit retrofit = builder.build();

        HighscoreClient highscoreClient = retrofit.create(HighscoreClient.class);
        Call<List<Highscore>> call = highscoreClient.getHighscore();

        return call;
    }

    public static Call<Highscore> getPostAsync(Highscore highscore) {
        System.out.println(highscore.getName() + " " + highscore.getScore());
        Retrofit retrofit = builder.build();

        HighscoreClient highscoreClient = retrofit.create(HighscoreClient.class);
        Call<Highscore> call = highscoreClient.createHighscore(highscore);

        return call;
    }
}
