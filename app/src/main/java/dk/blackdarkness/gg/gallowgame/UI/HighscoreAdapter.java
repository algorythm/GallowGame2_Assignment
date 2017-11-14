package dk.blackdarkness.gg.gallowgame.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import dk.blackdarkness.gg.R;
import dk.blackdarkness.gg.api.service.Highscore;

/**
 * Created by awo on 11/11/2017.
 */

public class HighscoreAdapter extends ArrayAdapter {
    public HighscoreAdapter(@NonNull Context context, List<Highscore> highscores) {
        super(context, R.layout.highscore_item, highscores);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View hsItem = inflater.inflate(R.layout.highscore_item, parent, false);

        final Highscore highscore = (Highscore) getItem(position);

        final TextView nameTextView = hsItem.findViewById(R.id.hsitem_name);
        final TextView scoreTextView = hsItem.findViewById(R.id.hsitem_score);
        final TextView wordTextView = hsItem.findViewById(R.id.hsitem_guessedWord);

        nameTextView.setText(highscore.getName());
        scoreTextView.setText(Double.toString(highscore.getScore()));
        wordTextView.setText(highscore.getWord());

        return hsItem;
    }
}
