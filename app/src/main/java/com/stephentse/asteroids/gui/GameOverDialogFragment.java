package com.stephentse.asteroids.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stephentse.asteroids.AsteroidsApplication;
import com.stephentse.asteroids.R;
import com.stephentse.asteroids.api.openshift.ICaller;
import com.stephentse.asteroids.system.SettingsWrapper;
import com.stephentse.asteroids.util.TypefaceFactory;

public class GameOverDialogFragment extends DialogFragment {

    public interface OnButtonClickListener {
        public void onClick();
    }

    private boolean _isWinnerDialog;
    private long _score;
    private String _name;
    private OnButtonClickListener _listener;

    private Typeface _spvTypeface;
    private Typeface _squareTypeface;
    private Button _dialogButton;
    private SubmitScoreView _submitScoreView;

    public GameOverDialogFragment() {
        super();
        _score = 0;
        _listener = null;
        _name = "";
        _isWinnerDialog = true;
    }


    public void enableWinnerDialog() {
        _isWinnerDialog = true;
    }

    public void enableGameOverDialog() {
        _isWinnerDialog = false;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void setScore(long score) {
        _score = score;
    }

    public long getScore() {
        return _score;
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        _listener = listener;
    }

    public OnButtonClickListener getOnButtonClickListener() {
        return _listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        _spvTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE_PUSH, getActivity().getAssets());
        _squareTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE, getActivity().getAssets());

        View dialogView = inflater.inflate(R.layout.dialog_gameover, null);
        TextView gameOverTitleTextView = (TextView)dialogView.findViewById(R.id.textViewGameOverTitle);
        gameOverTitleTextView.setTypeface(_spvTypeface);

        TextView winnerTitleTextView = (TextView)dialogView.findViewById(R.id.textViewWinnerTitle);
        winnerTitleTextView.setTypeface(_spvTypeface);

        TextView highScoreTextView = (TextView)dialogView.findViewById(R.id.textViewHighScore);
        highScoreTextView.setTypeface(_squareTypeface);

        View winnerLayout = dialogView.findViewById(R.id.layoutWinner);
        View gameOverLayout = dialogView.findViewById(R.id.layoutGameOver);
        if (_isWinnerDialog) {
            gameOverLayout.setVisibility(View.GONE);
        } else {
            winnerLayout.setVisibility(View.GONE);
        }

        String existingName = AsteroidsApplication.getSettings().getName();
        _submitScoreView = (SubmitScoreView)dialogView.findViewById(R.id.viewSubmit);
        _submitScoreView.setScore(_score);
        _submitScoreView.setName(existingName);

        builder.setView(dialogView)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       if (_listener != null) {
                           _listener.onClick();
                       }
                   }
               });
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //intercept and do nothing on back key press
                    return true;
                }
                return false;
            }
        });

        dialog.show();

        _dialogButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        _submitScoreView.setOnCanProgressListener(new SubmitScoreView.OnCanProgressListener() {
            @Override
            public void OnCanProgress(boolean canProgress) {
                if (canProgress) {
                    _dialogButton.setEnabled(true);
                }
                else {
                    _dialogButton.setEnabled(false);
                }
            }
        });
        _submitScoreView.setCaller(new ICaller() {
            @Override
            public boolean active() {
                return dialog.isShowing();
            }
        });

        boolean isHighScore = SettingsWrapper.isHighScore(_score);
        if (!isHighScore) {
            highScoreTextView.setVisibility(View.GONE);
            _submitScoreView.hideSubmitScore();
        }
        else {
            highScoreTextView.setVisibility(View.VISIBLE);
            _submitScoreView.showSubmitScore();
        }

        return dialog;
    }
}