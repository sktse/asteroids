package com.stephentse.asteroids;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stephentse.asteroids.gui.GameBoard;
import com.stephentse.asteroids.model.SpriteFactory;
import com.stephentse.asteroids.model.commands.CreateAsteroidCommand;
import com.stephentse.asteroids.system.Settings;
import com.stephentse.asteroids.system.SettingsWrapper;
import com.stephentse.asteroids.util.TypefaceFactory;

public class MainActivity extends Activity {
    private Handler _frame = new Handler();

    //Divide the frame by 1000 to calculate how many times per second the screen will update.
    private static final int FRAME_RATE = 20; //50 frames per second

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface forcedSquareTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.FORCED_SQUARE, getAssets());
        Typeface basicTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.BASIC, getAssets());
        Typeface squareTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE, getAssets());

        TextView titleTextView = (TextView) findViewById(R.id.textViewGameOverTitle);
        titleTextView.setTypeface(forcedSquareTypeface);

        Button startButton = (Button)findViewById(R.id.buttonStart);
        startButton.setTypeface(basicTypeface);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        Button helpButton = (Button)findViewById(R.id.buttonHelp);
        helpButton.setTypeface(basicTypeface);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
                startActivity(intent);
            }
        });

        TextView developedTextView = (TextView)findViewById(R.id.textViewDeveloped);
        developedTextView.setTypeface(squareTypeface);

        TextView meTextView = (TextView)findViewById(R.id.textViewStephen);
        meTextView.setTypeface(squareTypeface);

        TextView highScoreTitleTextView = (TextView)findViewById(R.id.textViewHighScoreTitle);
        highScoreTitleTextView.setTypeface(squareTypeface);

        long highScore = SettingsWrapper.getHighScore();
        TextView highScoreTextView = (TextView)findViewById(R.id.textViewHighScore);
        highScoreTextView.setTypeface(squareTypeface);
        highScoreTextView.setText(Long.toString(highScore));

        Settings settings = AsteroidsApplication.getSettings();
        String name = settings.getName();
        TextView nameTextView = (TextView)findViewById(R.id.textViewName);
        nameTextView.setTypeface(squareTypeface);
        nameTextView.setText(name);

        initializeAsteroids();
    }

    public void initializeAsteroids() {
        Point dimensions = getDimensions();
        Rect creationBounds = new Rect(0,0,dimensions.x,dimensions.y);

        CreateAsteroidCommand createSmallCommand = new CreateAsteroidCommand(30, SpriteFactory.AsteroidSize.SIZE_20, 4, null);
        CreateAsteroidCommand createMediumCommand = new CreateAsteroidCommand(60, SpriteFactory.AsteroidSize.SIZE_50, 3, null);
        CreateAsteroidCommand createLargeCommand = new CreateAsteroidCommand(100, SpriteFactory.AsteroidSize.SIZE_100, 2, null);

        GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);
        gameBoard.initializeAsteroids(createSmallCommand, creationBounds);
        gameBoard.initializeAsteroids(createMediumCommand, creationBounds);
        gameBoard.initializeAsteroids(createLargeCommand, creationBounds);

        _frame.removeCallbacks(frameUpdate);
        _frame.postDelayed(frameUpdate, FRAME_RATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //update the high score and name when we return to this activity
        long highScore = SettingsWrapper.getHighScore();
        TextView highScoreTextView = (TextView)findViewById(R.id.textViewHighScore);
        highScoreTextView.setText(Long.toString(highScore));

        String name = AsteroidsApplication.getSettings().getName();
        TextView nameTextView = (TextView)findViewById(R.id.textViewName);
        nameTextView.setText(name);

        _frame.removeCallbacks(frameUpdate);
        _frame.postDelayed(frameUpdate, FRAME_RATE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        _frame.removeCallbacks(frameUpdate);
    }

    private Point getDimensions() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }

    private Runnable frameUpdate = new Runnable() {
        @Override
        public synchronized void run() {
            _frame.removeCallbacks(frameUpdate);

            GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);
            gameBoard.tick();
            gameBoard.invalidate();

            _frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };
}
