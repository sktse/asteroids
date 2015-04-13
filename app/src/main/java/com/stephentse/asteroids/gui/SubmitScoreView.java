package com.stephentse.asteroids.gui;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.stephentse.asteroids.util.TypefaceFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class SubmitScoreView extends FrameLayout {

    private enum UploadProgress { NotUploaded, Uploading, Uploaded }

    private Typeface _basicTypeface;
    private Typeface _squareTypeface;

    private EditText _nameEditText;
    private ProgressBar _uploadProgressBar;
    private ImageView _checkImageView;
    private Button _uploadButton;
    private TextView _scoreTextView;
    private TextView _nameSubTitleTextView;
    private View _submitLayout;

    private String _name;
    private long _score;
    private boolean _isHideSubmitScore = false;

    private OnCanProgressListener _listener;
    private ICaller _caller;

    public SubmitScoreView(Context context) {
        super(context);

        initialize(context);
    }

    public SubmitScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context);
    }

    public SubmitScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context);
    }

    private void initialize(Context context) {
        _listener = null;
        setCaller(new Caller());

        if (!isInEditMode()) {
            _basicTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.BASIC, context.getAssets());
            _squareTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE, context.getAssets());
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.layout_score, null));

        _submitLayout = findViewById(R.id.layoutNameAndButton);

        TextView titleTextView = (TextView)findViewById(R.id.textViewTitle);
        if (!isInEditMode()) {
            titleTextView.setTypeface(_basicTypeface);
        }

        _nameSubTitleTextView = (TextView)findViewById(R.id.textViewName);
        if (!isInEditMode()) {
            _nameSubTitleTextView.setTypeface(_basicTypeface);
        }

        //score text view
        _scoreTextView = (TextView)findViewById(R.id.textViewScore);
        _scoreTextView.setText(Long.toString(_score));
        if (!isInEditMode()) {
            _scoreTextView.setTypeface(_squareTypeface);
        }

        //name edit text
        _nameEditText = (EditText)findViewById(R.id.editTextName);
        if (!isInEditMode()) {
            _nameEditText.setTypeface(_squareTypeface);
        }
        if (_name != null) {
            _nameEditText.setText(_name);
            _nameEditText.setSelection(_name.length());
        }
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
                    if (_listener != null) {
                        _listener.OnCanProgress(false);
                    }
                } else {
                    _nameEditText.setError(null);
                    if (_listener != null) {
                        _listener.OnCanProgress(true);
                    }
                }

                _name = _nameEditText.getText().toString();
            }
        });

        _uploadProgressBar = (ProgressBar)findViewById(R.id.progressBarUpload);
        _checkImageView = (ImageView)findViewById(R.id.imageViewCheck);
        _uploadButton = (Button)findViewById(R.id.buttonUpload);
        _uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNameValid()) {
                    //if the name is not valid, do not ping the server
                    return;
                }

                //disable the ok button on the dialog
                setUploadProgressViews(UploadProgress.Uploading);
                final String name = _nameEditText.getText().toString();
                final String userId = AsteroidsApplication.getSettings().getUserId();
                OpenshiftServerApi.getApi().postScore(userId, name, _score,
                        new ApiResponseHandler(getCaller(),
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

        if (_isHideSubmitScore) {
            hideSubmitScore();
        }
        else {
            showSubmitScore();
        }
    }

    private boolean isNameValid() {
        String name = _nameEditText.getText().toString();
        if (name.length() == 0) {
            String emptyNameErrorString = getResources().getString(R.string.high_score_empty);
            _nameEditText.setError(emptyNameErrorString);
            return false;
        }

        return true;
    }

    public void setOnCanProgressListener(OnCanProgressListener listener) {
        _listener = listener;
    }

    public synchronized void setCaller(ICaller caller) {
        _caller = caller;
    }

    public synchronized ICaller getCaller() {
        return _caller;
    }

    public void setName(String name) {
        _name = name;

        if (_nameEditText != null) {
            _nameEditText.setText(_name);
            _nameEditText.setSelection(_name.length());
        }
    }

    public String getName() {
        return _name;
    }

    public void setScore(long score) {
        _score = score;

        if (_scoreTextView != null) {
            _scoreTextView.setText(Long.toString(_score));
        }
    }

    public long getScore() {
        return _score;
    }

    public void hideSubmitScore() {
        _isHideSubmitScore = true;

        if (_submitLayout != null && _nameSubTitleTextView != null) {
            _submitLayout.setVisibility(View.GONE);
            _nameSubTitleTextView.setVisibility(View.GONE);
        }
    }

    public void showSubmitScore() {
        _isHideSubmitScore = false;

        if (_submitLayout != null && _nameSubTitleTextView != null) {
            _submitLayout.setVisibility(View.VISIBLE);
            _nameSubTitleTextView.setVisibility(View.VISIBLE);
        }
    }

    private void setUploadProgressViews(UploadProgress progress) {
        switch (progress) {
            case NotUploaded:
                if (_listener != null) {
                    _listener.OnCanProgress(true);
                }
                _uploadButton.setVisibility(View.VISIBLE);
                _uploadProgressBar.setVisibility(View.GONE);
                _checkImageView.setVisibility(View.GONE);
                _nameEditText.setEnabled(true);
                break;
            case Uploading:
                if (_listener != null) {
                    _listener.OnCanProgress(false);
                }
                _uploadButton.setVisibility(View.GONE);
                _uploadProgressBar.setVisibility(View.VISIBLE);
                _checkImageView.setVisibility(View.GONE);
                _nameEditText.setEnabled(false);
                break;
            case Uploaded:
            default:
                if (_listener != null) {
                    _listener.OnCanProgress(true);
                }
                _uploadButton.setVisibility(View.GONE);
                _uploadProgressBar.setVisibility(View.GONE);
                _checkImageView.setVisibility(View.VISIBLE);
                _nameEditText.setEnabled(false);
                break;
        }
    }

    private class Caller implements ICaller {
        @Override
        public boolean active() {
            return SubmitScoreView.this.getVisibility() == View.VISIBLE;
        }
    }

    public interface OnCanProgressListener {
        public void OnCanProgress(boolean canProgress);
    }
}
