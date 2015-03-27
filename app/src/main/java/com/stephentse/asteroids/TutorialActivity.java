package com.stephentse.asteroids;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.widget.TextView;

import com.stephentse.asteroids.gui.GameBoard;
import com.stephentse.asteroids.gui.GameEvent;
import com.stephentse.asteroids.gui.OnGameEventListener;
import com.stephentse.asteroids.gui.TutorialPausedDialogFragment;
import com.stephentse.asteroids.model.SpriteFactory;
import com.stephentse.asteroids.model.commands.CreateCyclingAsteroidCommand;
import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.ISprite;
import com.stephentse.asteroids.util.TypefaceFactory;

public class TutorialActivity extends FragmentActivity {
    private Handler _frame = new Handler();

    private TextView _scoreTextView;
    private TextView _multiplierTextView;

    private long _score;
    private float _multiplier;

    //Divide the frame by 1000 to calculate how many times per second the screen will update.
    private static final int FRAME_RATE = 20; //50 frames per second
    private static final int PLAYER_RESPAWN_DELAY = 1000;

    private static final String TUTORIAL_PAUSED_FRAGMENT_NAME = "TUTORIAL_PAUSED_FRAGMENT_NAME";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        Typeface typeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE, getAssets());
        _scoreTextView = (TextView)findViewById(R.id.textViewScore);
        _scoreTextView.setTypeface(typeface);

        _multiplierTextView = (TextView)findViewById(R.id.textViewMultiplier);
        _multiplierTextView.setTypeface(typeface);

        Typeface tutorialTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.FORCED_SQUARE, getAssets());
        TextView lifeTutorialTextView = (TextView)findViewById(R.id.textViewTutorialLives);
        lifeTutorialTextView.setTypeface(tutorialTypeface);

        TextView scoreTutorialTextVIew = (TextView)findViewById(R.id.textViewTutorialScore);
        scoreTutorialTextVIew.setTypeface(tutorialTypeface);

        TextView multiplierTutorialTextView = (TextView)findViewById(R.id.textViewTutorialMultiplier);
        multiplierTutorialTextView.setTypeface(tutorialTypeface);

        TextView playerTutorialTextView = (TextView)findViewById(R.id.textViewTutorialPlayer);
        playerTutorialTextView.setTypeface(tutorialTypeface);

        TextView asteroidTutorialTextView = (TextView)findViewById(R.id.textViewTutorialAsteroid);
        asteroidTutorialTextView.setTypeface(tutorialTypeface);

        final GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);
        gameBoard.setOnGameEventListener(new OnGameEventListener() {
            @Override
            public void onGameEvent(int event, ISprite sprite) {
                String multiplierString = getResources().getString(R.string.multiplier_format);
                switch(event) {
                    case GameEvent.ASTEROID_DESTROYED:
                        Asteroid asteroid = (Asteroid) sprite;
                        _score += (asteroid.getPoints() * _multiplier);
                        _multiplier += 0.1;

                        _scoreTextView.setText(Long.toString(_score));
                        _multiplierTextView.setText(String.format(multiplierString, _multiplier));
                        break;
                    case GameEvent.PLAYER_REKTD:
                        _multiplier = 1.0f;
                        _multiplierTextView.setText(String.format(multiplierString, _multiplier));
                        _frame.postDelayed(playerRespawn, PLAYER_RESPAWN_DELAY);
                    case GameEvent.GAME_COMPLETE:
                        //should be impossible!
                        //asteroids are designed to spawn in a never ending cycle
                        break;
                    default:
                        throw new IllegalArgumentException("Unrecoginzed GameEvent value: " + event);
                }
            }
        });


        initializeGame();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!AsteroidsApplication.getInstance().getGameStatus().isPaused()) {
            _frame.removeCallbacks(frameUpdate);
            _frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        _frame.removeCallbacks(frameUpdate);
        if (!AsteroidsApplication.getInstance().getGameStatus().isPaused()) {
            pauseGame();
        }
    }

    public synchronized void initializeGame() {
        Point dimensions = getDimensions();
        Point playerPosition = new Point((int)(dimensions.x * 0.5) - 30, (int)(dimensions.y * 0.8) - 30);
        Rect creationBounds = new Rect(0,0,dimensions.x,(int) (dimensions.y * 0.6));

        CreateCyclingAsteroidCommand asteroidCommand = new CreateCyclingAsteroidCommand(100, SpriteFactory.AsteroidSize.SIZE_100, new Point(50, 400), new Point(5, 0));

        GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);
        gameBoard.resetStarField();
        gameBoard.initializeGame(playerPosition, 15, 10, 20, asteroidCommand, creationBounds);

        _score = 0;
        _multiplier = 1;
        _scoreTextView.setText(Long.toString(_score));
        String multiplierString = getResources().getString(R.string.multiplier_format);
        _multiplierTextView.setText(String.format(multiplierString, _multiplier));

        AsteroidsApplication.getInstance().getGameStatus().resumeGame();
        _frame.removeCallbacks(frameUpdate);
        _frame.postDelayed(frameUpdate, FRAME_RATE);
    }

    private void pauseGame() {
        final TutorialPausedDialogFragment fragment = new TutorialPausedDialogFragment();
        fragment.setOnDialogDismissedListener(new TutorialPausedDialogFragment.OnDialogDismissedListener() {
            @Override
            public void onDismiss(int event) {
                switch(event) {
                    case TutorialPausedDialogFragment.RESUME_EVENT:
                        fragment.dismiss();
                        AsteroidsApplication.getInstance().getGameStatus().resumeGame();
                        _frame.removeCallbacks(frameUpdate);
                        _frame.postDelayed(frameUpdate, FRAME_RATE);
                        break;
                    case TutorialPausedDialogFragment.QUIT_EVENT:
                        fragment.dismiss();
                        _frame.removeCallbacks(frameUpdate);
                        TutorialActivity.this.finish();
                    default:
                        return;
                }
            }
        });
        fragment.show(getFragmentManager(), TUTORIAL_PAUSED_FRAGMENT_NAME);

        AsteroidsApplication.getInstance().getGameStatus().pauseGame();
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
            if (AsteroidsApplication.getInstance().getGameStatus().isPaused()) {
                return;
            }

            GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);
            gameBoard.tick();
            gameBoard.invalidate();
            _frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };

    private Runnable playerRespawn = new Runnable() {
        @Override
        public synchronized void run() {
            _frame.removeCallbacks(playerRespawn);
            Point dimensions = getDimensions();
            Point playerPosition = new Point((int)(dimensions.x * 0.5) - 30, (int)(dimensions.y * 0.8) - 30);

            GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);
            gameBoard.continueGame(playerPosition, 15, 10, 20);
        }
    };

    @Override
    public void onBackPressed() {
        pauseGame();
        //intercept default back press behaviour
        //pause the game and pop the menu
    }
}
