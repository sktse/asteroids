package com.stephentse.asteroids;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.stephentse.asteroids.gui.GameBoard;
import com.stephentse.asteroids.gui.GameEvent;
import com.stephentse.asteroids.gui.GameOverDialogFragment;
import com.stephentse.asteroids.gui.OnGameEventListener;
import com.stephentse.asteroids.gui.PausedDialogFragment;
import com.stephentse.asteroids.model.SpriteFactory;
import com.stephentse.asteroids.model.commands.CreateAsteroidCommand;
import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.ISprite;
import com.stephentse.asteroids.system.SettingsWrapper;
import com.stephentse.asteroids.system.gameDifficulty.GameDifficulty;
import com.stephentse.asteroids.util.TypefaceFactory;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends FragmentActivity {
    private Handler _frame = new Handler();

    private static final String WINNER_FRAGMENT_NAME = "WINNER_FRAGMENT_NAME";
    private static final String GAME_OVER_FRAGMENT_NAME = "GAME_OVER_FRAGMENT_NAME";
    private static final String PAUSED_FRAGMENT_NAME = "PAUSED_FRAGMENT_NAME";

    //Divide the frame by 1000 to calculate how many times per second the screen will update.
    private int _frameRate = 50;
    private float _difficultyMultiplier = 1.0f;

    private static final int PLAYER_RESPAWN_DELAY = 1000;

    private long _score;
    private float _multiplier;
    private int _lives;

    private TextView _scoreTextView;
    private TextView _multiplierTextView;
    private List<FrameLayout> _lifeLayouts;

    private Bitmap _shipBitmap;
    private Bitmap _bulletBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        _frameRate = getIntent().getIntExtra(GameDifficulty.FRAME_RATE_KEY, GameDifficulty.FRAME_RATE_DEFAULT);
        _difficultyMultiplier = getIntent().getFloatExtra(GameDifficulty.MULTIPLIER_KEY, GameDifficulty.MULTIPLIER_DEFAULT);

        _shipBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player30);
        _bulletBitmap = BitmapFactory.decodeResource(
                AsteroidsApplication.getInstance().getResources(), R.drawable.bullet);

        _lifeLayouts = new ArrayList<FrameLayout>();
        _lifeLayouts.add((FrameLayout) findViewById(R.id.layoutLife1));
        _lifeLayouts.add((FrameLayout) findViewById(R.id.layoutLife2));
        _lifeLayouts.add((FrameLayout) findViewById(R.id.layoutLife3));

        Typeface typeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE, getAssets());
        _scoreTextView = (TextView) findViewById(R.id.textViewScore);
        _scoreTextView.setTypeface(typeface);

        _multiplierTextView = (TextView) findViewById(R.id.textViewMultiplierTitle);
        _multiplierTextView.setTypeface(typeface);

        final GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);
        gameBoard.setOnGameEventListener(new OnGameEventListener() {
            @Override
            public void onGameEvent(int event, ISprite sprite) {
                String multiplierString = getResources().getString(R.string.multiplier_format);
                switch(event) {
                    case GameEvent.ASTEROID_DESTROYED:
                        //An asteroid has been destroyed
                        //So award the points and increment the necessary multipliers
                        Asteroid asteroid = (Asteroid) sprite;
                        _score += (asteroid.getPoints() * _multiplier * _difficultyMultiplier);
                        _multiplier += 0.1;

                        _scoreTextView.setText(Long.toString(_score));
                        _multiplierTextView.setText(String.format(multiplierString, _multiplier));
                        break;
                    case GameEvent.PLAYER_REKTD:
                        //Player has lost a life
                        _lives--;
                        updatePlayerLives();
                        if (_lives > 0) {
                            //Player has another life, spawn another player ship
                            _multiplier = 1.0f;
                            _multiplierTextView.setText(String.format(multiplierString, _multiplier));
                            _frame.postDelayed(playerRespawn, PLAYER_RESPAWN_DELAY);
                        } else {
                            //Player has no more lives. Game over!
                            AsteroidsApplication.getInstance().getGameStatus().pauseGame();
                            String name = AsteroidsApplication.getSettings().getName();
                            final GameOverDialogFragment fragment = new GameOverDialogFragment();
                            fragment.enableGameOverDialog();
                            fragment.setScore(_score);
                            fragment.setName(name);
                            fragment.setOnButtonClickListener(new GameOverDialogFragment.OnButtonClickListener() {
                                @Override
                                public void onClick() {
                                    //Ok clicked
                                    SettingsWrapper.setIfHighScore(_score);
                                    String newName = fragment.getName();
                                    AsteroidsApplication.getSettings().setName(newName);
                                    finish();
                                }
                            });
                            fragment.show(getFragmentManager(), GAME_OVER_FRAGMENT_NAME);
                        }
                        break;
                    case GameEvent.GAME_COMPLETE:
                        //Player has destroyed all asteroids. They win!
                        AsteroidsApplication.getInstance().getGameStatus().pauseGame();
                        String name = AsteroidsApplication.getSettings().getName();
                        final GameOverDialogFragment fragment = new GameOverDialogFragment();
                        fragment.enableWinnerDialog();
                        fragment.setScore(_score);
                        fragment.setName(name);
                        fragment.setOnButtonClickListener(new GameOverDialogFragment.OnButtonClickListener() {
                            @Override
                            public void onClick() {
                                //Ok clicked
                                SettingsWrapper.setIfHighScore(_score);
                                String newName = fragment.getName();
                                AsteroidsApplication.getSettings().setName(newName);
                                finish();
                            }
                        });
                        fragment.show(getFragmentManager(), WINNER_FRAGMENT_NAME);
                        break;
                    default:
                        throw new IllegalArgumentException("Unrecognized GameEvent value: " + event);
                }
            }
        });

        initializeGame();
    }

    public synchronized void initializeGame() {
        Point dimensions = getDimensions();
        Point playerPosition = new Point((int)(dimensions.x * 0.5), (int)(dimensions.y * 0.8));
        Rect creationBounds = new Rect(0,0,dimensions.x,(int) (dimensions.y * 0.6));

        CreateAsteroidCommand createSmallCommand = new CreateAsteroidCommand(30, SpriteFactory.AsteroidSize.SIZE_20, 4, null);
        CreateAsteroidCommand createMediumCommand = new CreateAsteroidCommand(60, SpriteFactory.AsteroidSize.SIZE_50, 3, createSmallCommand);
        CreateAsteroidCommand createLargeCommand = new CreateAsteroidCommand(100, SpriteFactory.AsteroidSize.SIZE_100, 2, createMediumCommand);

        GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);
        gameBoard.resetStarField();
        gameBoard.initializeGame(playerPosition, 15, 10, 20, createLargeCommand, creationBounds, _shipBitmap, _bulletBitmap);

        _score = 0;
        _multiplier = 1;
        _lives = 3;
        _scoreTextView.setText(Long.toString(_score));
        String multiplierString = getResources().getString(R.string.multiplier_format);
        _multiplierTextView.setText(String.format(multiplierString, _multiplier));

        for (int i = 0; i < _lifeLayouts.size(); i++) {
            FrameLayout life = _lifeLayouts.get(i);
            life.setVisibility(View.VISIBLE);
        }

        AsteroidsApplication.getInstance().getGameStatus().resumeGame();
        _frame.removeCallbacks(frameUpdate);
        _frame.postDelayed(frameUpdate, _frameRate);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!AsteroidsApplication.getInstance().getGameStatus().isPaused()) {
            _frame.removeCallbacks(frameUpdate);
            _frame.postDelayed(frameUpdate, _frameRate);
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

    private void updatePlayerLives() {
        for (int i = _lifeLayouts.size(); i > _lives; i--) {
            FrameLayout life = _lifeLayouts.get(i - 1);
            life.setVisibility(View.INVISIBLE);
        }
    }

    private void pauseGame() {
        final PausedDialogFragment fragment = new PausedDialogFragment();
        fragment.setOnDialogDismissedListener(new PausedDialogFragment.OnDialogDismissedListener() {
            @Override
            public void onDismiss(int event) {
                switch(event) {
                    case PausedDialogFragment.RESET_EVENT:
                        //Player has pressed restart game
                        fragment.dismiss();
                        initializeGame();
                        _frame.removeCallbacks(frameUpdate);
                        _frame.postDelayed(frameUpdate, _frameRate);
                        break;
                    case PausedDialogFragment.RESUME_EVENT:
                        //Player has resumed the game
                        fragment.dismiss();
                        AsteroidsApplication.getInstance().getGameStatus().resumeGame();
                        _frame.removeCallbacks(frameUpdate);
                        _frame.postDelayed(frameUpdate, _frameRate);
                        break;
                    case PausedDialogFragment.QUIT_EVENT:
                        //Player has quit the game entirely
                        fragment.dismiss();
                        _frame.removeCallbacks(frameUpdate);
                        GameActivity.this.finish();
                    default:
                        return;
                }
            }
        });
        fragment.show(getFragmentManager(), PAUSED_FRAGMENT_NAME);

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
            _frame.postDelayed(frameUpdate, _frameRate);
        }
    };

    private Runnable playerRespawn = new Runnable() {
        @Override
        public synchronized void run() {
            _frame.removeCallbacks(playerRespawn);
            Point dimensions = getDimensions();
            Point playerPosition = new Point((int)(dimensions.x * 0.5), (int)(dimensions.y * 0.8));


            GameBoard gameBoard = (GameBoard)findViewById(R.id.gameBoard);
            gameBoard.continueGame(playerPosition, 15, 10, 20, _shipBitmap, _bulletBitmap);
        }
    };

    @Override
    public void onBackPressed() {
        pauseGame();
        //intercept default back press behaviour
        //pause the game and pop the menu
    }
}
