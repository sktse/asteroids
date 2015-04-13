package com.stephentse.asteroids;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.stephentse.asteroids.gui.GameBoard;
import com.stephentse.asteroids.gui.SubmitScoreDialogFragment;
import com.stephentse.asteroids.gui.TypefaceStringArrayAdapter;
import com.stephentse.asteroids.system.SettingsWrapper;
import com.stephentse.asteroids.system.gameDifficulty.GameDifficulty;
import com.stephentse.asteroids.system.gameDifficulty.GameDifficultyFactory;
import com.stephentse.asteroids.util.TypefaceFactory;

import java.util.ArrayList;

public class OptionsActivity extends FragmentActivity {

    private static final String SUBMIT_SCORE_DIALOG_KEY = "SUBMIT_SCORE_DIALOG_KEY";

    private ArrayList<Integer> _multiplierColors;
    private GameBoard _gameBoard;
    private Spinner _difficultySpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        _multiplierColors = new ArrayList<Integer>();
        _multiplierColors.add(getResources().getColor(R.color.dark_red));
        _multiplierColors.add(getResources().getColor(R.color.orange));
        _multiplierColors.add(getResources().getColor(R.color.yellow));
        _multiplierColors.add(getResources().getColor(R.color.bright_green));

        Typeface forcedSquareTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.FORCED_SQUARE, getAssets());
        Typeface basicTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.BASIC, getAssets());
        Typeface squareTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE, getAssets());

        _gameBoard = (GameBoard)findViewById(R.id.gameBoard);

        //Title
        TextView optionsTextView = (TextView)findViewById(R.id.textViewOptions);
        optionsTextView.setTypeface(forcedSquareTypeface);

        //Difficulty
        TextView multiplierTitleTextView = (TextView)findViewById(R.id.textViewMultiplierTitle);
        multiplierTitleTextView.setTypeface(squareTypeface);

        final TextView multiplierTextView = (TextView)findViewById(R.id.textViewMultiplier);
        multiplierTextView.setTypeface(squareTypeface);

        TextView difficultyTextView = (TextView)findViewById(R.id.textViewDifficulty);
        difficultyTextView.setTypeface(squareTypeface);

        String[] gameDifficultyStrings = getResources().getStringArray(R.array.game_difficulties);
        TypefaceStringArrayAdapter dataAdapter = new TypefaceStringArrayAdapter(
                this, android.R.layout.simple_spinner_item, gameDifficultyStrings);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.setTypeface(squareTypeface);
        _difficultySpinner = (Spinner)findViewById(R.id.spinnerDifficulty);
        _difficultySpinner.setAdapter(dataAdapter);
        _difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GameDifficulty.DifficultyType[] difficultyTypes = GameDifficulty.DifficultyType.values();
                GameDifficulty.DifficultyType selectedDifficultyType = difficultyTypes[position];
                GameDifficulty difficulty = GameDifficultyFactory.getDifficulty(selectedDifficultyType);

                String multiplierString = getResources().getString(R.string.multiplier_format);
                multiplierTextView.setText(String.format(multiplierString, difficulty.getDifficultyMultiplier()));
                multiplierTextView.setTextColor(_multiplierColors.get(position));

                SettingsWrapper.setGameDifficulty(selectedDifficultyType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                GameDifficulty.DifficultyType type = SettingsWrapper.getGameDifficulty();
                int index = getIndexOfDifficultyType(type);
                _difficultySpinner.setSelection(index);
            }
        });

        //High Score
        TextView highScoreTextView = (TextView)findViewById(R.id.textViewHighScore);
        highScoreTextView.setTypeface(squareTypeface);

        Button scoreButton = (Button)findViewById(R.id.buttonScore);
        scoreButton.setTypeface(basicTypeface);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SubmitScoreDialogFragment fragment = new SubmitScoreDialogFragment();
                fragment.show(getFragmentManager(), SUBMIT_SCORE_DIALOG_KEY);

            }
        });

        //OK Button
        Button okButton = (Button)findViewById(R.id.buttonOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initialize();
    }

    private void initialize() {
        _gameBoard.setNumberOfStars(100);

        GameDifficulty.DifficultyType type = SettingsWrapper.getGameDifficulty();
        int index = getIndexOfDifficultyType(type);
        _difficultySpinner.setSelection(index);
    }

    private int getIndexOfDifficultyType(GameDifficulty.DifficultyType type) {
        GameDifficulty.DifficultyType[] types = GameDifficulty.DifficultyType.values();
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(type)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unknown DifficultyType " + type);
    }
}
