package dk.blackdarkness.gg.api.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by awo on 11/11/2017.
 */

public interface HighscoreClient {
    @GET("/highscores/{id}")
    Call<Highscore> getHighscore(@Path("id") int id);

    @GET("highscores")
    Call<List<Highscore>> getHighscore();

    @POST("highscores")
    Call<Highscore> createHighscore(@Body Highscore highscore);
}
