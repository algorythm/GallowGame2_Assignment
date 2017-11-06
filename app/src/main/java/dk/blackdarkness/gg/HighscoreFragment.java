package dk.blackdarkness.gg;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awo on 06/11/2017.
 */

public class HighscoreFragment extends Fragment {
    private List<HighscoreDTO> highscores;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highscores, container, false);

        this.highscores = FetchHighscoresAsync();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private ArrayList<HighscoreDTO> FetchHighscoresAsync()  {
        // TODO: Implement method
        return null;
    }
}
