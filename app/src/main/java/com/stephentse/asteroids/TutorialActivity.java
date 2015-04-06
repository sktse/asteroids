package com.stephentse.asteroids;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
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
    private Bitmap _shipBitmap;
    private Bitmap _bulletBitmap;

    private boolean _isInitialized = false;
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

        _shipBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player30);
        _bulletBitmap = BitmapFactory.decodeResource(
                AsteroidsApplication.getInstance().getResources(), R.drawable.bullet);

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

        TextView shipTutorialTextView = (TextView)findViewById(R.id.textViewTutorialShip);
        shipTutorialTextView.setTypeface(tutorialTypeface);

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
                        throw new IllegalArgumentException("Unrecognized GameEvent value: " + event);
                }
            }
        });

        final View contentView = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        ViewTreeObserver viewTreeObserver = contentView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //The tutorial overlay is done ahead of time and entirely based on a screen height that
                //includes the top Android system status bar.
                //We need to wait until the whole view is laid out so we can measure the correct
                //spot to load the player ship to fit within the tutorial overlay.
                if (!_isInitialized) {
                    //this can be triggered multiple times, but we need to only start the game once
                    _isInitialized = true;
                    initializeGame();
                }
            }
        });
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
        int playerPositionX = (int)(dimensions.x * 0.5) - (int) (_shipBitmap.getWidth() * 0.5);
        int playerPositionY = (int)(dimensions.y * 0.9) - (_shipBitmap.getHeight()) - 5;
        Point playerPosition = new Point(playerPositionX, playerPositionY);

        Rect creationBounds = new Rect(0,0,dimensions.x,(int) (dimensions.y * 0.6));

        Bitmap asteroidBitmap = SpriteFactory.getAsteroidBitmap(SpriteFactory.AsteroidSize.SIZE_100);
        int asteroidPositionX = (int)(dimensions.x * 0.5);
        int asteroidPositionY = (int)(dimensions.y * 0.5) - asteroidBitmap.getHeight() - 10;
        CreateCyclingAsteroidCommand asteroidCommand = new CreateCyclingAsteroidCommand(100, SpriteFactory.AsteroidSize.SIZE_100, new Point(asteroidPositionX, asteroidPositionY), new Point(5, 0));

        GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);
        gameBoard.resetStarField();
        gameBoard.initializeGame(playerPosition, 15, 10, 20, asteroidCommand, creationBounds, _shipBitmap, _bulletBitmap);

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
            int playerPositionX = (int)(dimensions.x * 0.5) - (int) (_shipBitmap.getWidth() * 0.5);
            int playerPositionY = (int)(dimensions.y * 0.9) - (_shipBitmap.getHeight()) - 5;
            Point playerPosition = new Point(playerPositionX, playerPositionY);

            GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);
            gameBoard.continueGame(playerPosition, 15, 10, 20, _shipBitmap, _bulletBitmap);
        }
    };

    private Point getDimensions() {
        //this will only work if the views have already been laid out
        View contentView = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        return new Point(contentView.getWidth(), contentView.getHeight());
    }

    @Override
    public void onBackPressed() {
        pauseGame();
        //intercept default back press behaviour
        //pause the game and pop the menu
    }
}
