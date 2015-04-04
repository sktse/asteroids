package com.stephentse.asteroids.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stephentse.asteroids.AsteroidsApplication;
import com.stephentse.asteroids.R;
import com.stephentse.asteroids.api.crashlytics.CrashlyticsWrapper;
import com.stephentse.asteroids.api.openshift.ApiResponseHandler;
import com.stephentse.asteroids.api.openshift.ICaller;
import com.stephentse.asteroids.api.openshift.InnerApiResponseHandler;
import com.stephentse.asteroids.api.openshift.OpenshiftServerApi;
import com.stephentse.asteroids.system.Settings;
import com.stephentse.asteroids.system.SettingsWrapper;
import com.stephentse.asteroids.util.TypefaceFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class GameOverDialogFragment extends DialogFragment {

    public interface OnButtonClickListener {
        public void onClick();
    }

    private enum UploadProgress { NotUploaded, Uploading, Uploaded }

    private boolean _isWinnerDialog;
    private long _score;
    private String _name;
    private OnButtonClickListener _listener;

    private EditText _nameEditText;
    private Button _dialogButton;
    private Button _uploadButton;
    private ProgressBar _uploadProgressBar;
    private ImageView _checkImageView;

    private Typeface _spvTypeface;
    private Typeface _basicTypeface;
    private Typeface _squareTypeface;

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
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        _spvTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE_PUSH, getActivity().getAssets());
        _basicTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.BASIC, getActivity().getAssets());
        _squareTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE, getActivity().getAssets());

        View dialogView = inflater.inflate(R.layout.dialog_gameover, null);
        TextView gameOverTitleTextView = (TextView)dialogView.findViewById(R.id.textViewGameOverTitle);
        gameOverTitleTextView.setTypeface(_spvTypeface);

        TextView winnerTitleTextView = (TextView)dialogView.findViewById(R.id.textViewWinnerTitle);
        winnerTitleTextView.setTypeface(_spvTypeface);

        View winnerLayout = dialogView.findViewById(R.id.layoutWinner);
        View gameOverLayout = dialogView.findViewById(R.id.layoutGameOver);
        if (_isWinnerDialog) {
            gameOverLayout.setVisibility(View.GONE);
        } else {
            winnerLayout.setVisibility(View.GONE);
        }

        TextView subtitleTextView = (TextView)dialogView.findViewById(R.id.textViewSubtitle);
        subtitleTextView.setTypeface(_basicTypeface);

        TextView scoreTextView = (TextView)dialogView.findViewById(R.id.textViewScore);
        scoreTextView.setTypeface(_squareTypeface);
        scoreTextView.setText(Long.toString(_score));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       if (_listener != null) {
                           _listener.onClick();
                       }
                   }
               });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        _dialogButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        initializeHighScoreViews(dialogView, dialog, SettingsWrapper.isHighScore(_score));

        return dialog;
    }

    private void initializeHighScoreViews(View dialogView, final AlertDialog dialog, boolean isVisible) {
        //high score related views
        View nameAndButtonLayout = dialogView.findViewById(R.id.layoutNameAndButton);

        class Caller implements ICaller {
            @Override
            public boolean active() {
                return dialog.isShowing();
            }
        }

        TextView highScoreTextView = (TextView)dialogView.findViewById(R.id.textViewHighScore);
        highScoreTextView.setTypeface(_squareTypeface);

        TextView nameSubTitleTextView = (TextView)dialogView.findViewById(R.id.textViewName);
        nameSubTitleTextView.setTypeface(_basicTypeface);

        _nameEditText = (EditText)dialogView.findViewById(R.id.editTextName);
        _nameEditText.setTypeface(_squareTypeface);
        _nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //block all spaces from name inputs
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    _nameEditText.setText(result);
                    _nameEditText.setSelection(result.length());
                }

                if (_nameEditText.length() == 0) {
                    String emptyNameErrorString = getResources().getString(R.string.high_score_empty);
                    _nameEditText.setError(emptyNameErrorString);
                    _dialogButton.setEnabled(false);
                } else {
                    _nameEditText.setError(null);
                    _dialogButton.setEnabled(true);
                }

                _name = _nameEditText.getText().toString();
            }
        });

        final String userId = AsteroidsApplication.getSettings().getUserId();

        _uploadProgressBar = (ProgressBar)dialogView.findViewById(R.id.progressBarUpload);
        _checkImageView = (ImageView)dialogView.findViewById(R.id.imageViewCheck);
        _uploadButton = (Button)dialogView.findViewById(R.id.buttonUpload);
        _uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disable the ok button on the dialog
                setUploadProgressViews(UploadProgress.Uploading);
                final String name = _nameEditText.getText().toString();
                OpenshiftServerApi.getApi().postScore(userId, name, _score,
                        new ApiResponseHandler(new Caller(),
                                new InnerApiResponseHandler() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        setUploadProgressViews(UploadProgress.Uploaded);
                                        try {
                                            String userId = result.getString("user_id");
                                            Settings settings = AsteroidsApplication.getSettings();
                                            settings.setUserId(userId);
                                        } catch (JSONException e) {
                                            CrashlyticsWrapper.logException("Game over dialog missing user_id in response", e);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable e, JSONObject response) {
                                        setUploadProgressViews(UploadProgress.NotUploaded);

                                        String connectionErrorString = getResources().getString(R.string.high_score_connection_error);
                                        _nameEditText.setError(connectionErrorString);
                                        CrashlyticsWrapper.logException("Game over dialog failed to get score", response, e);
                                    }

                                    @Override
                                    public void onFailure(Throwable e) {
                                        setUploadProgressViews(UploadProgress.NotUploaded);

                                        String connectionErrorString = getResources().getString(R.string.high_score_connection_error);
                                        _nameEditText.setError(connectionErrorString);
                                        CrashlyticsWrapper.logException("Game over dialog failed to get score", e);
                                    }
                                }
                        )
                );

            }
        });

        if (!isVisible) {
            highScoreTextView.setVisibility(View.GONE);
            nameSubTitleTextView.setVisibility(View.GONE);
            nameAndButtonLayout.setVisibility(View.GONE);
        }
    }

    private void setUploadProgressViews(UploadProgress progress) {
        switch (progress) {
            case NotUploaded:
                _dialogButton.setEnabled(true);
                _uploadButton.setVisibility(View.VISIBLE);
                _uploadProgressBar.setVisibility(View.GONE);
                _checkImageView.setVisibility(View.GONE);
                _nameEditText.setEnabled(true);
                break;
            case Uploading:
                _dialogButton.setEnabled(false);
                _uploadButton.setVisibility(View.GONE);
                _uploadProgressBar.setVisibility(View.VISIBLE);
                _checkImageView.setVisibility(View.GONE);
                _nameEditText.setEnabled(false);
                break;
            case Uploaded:
            default:
                _dialogButton.setEnabled(true);
                _uploadButton.setVisibility(View.GONE);
                _uploadProgressBar.setVisibility(View.GONE);
                _checkImageView.setVisibility(View.VISIBLE);
                _nameEditText.setEnabled(false);
                break;
        }
    }

}
