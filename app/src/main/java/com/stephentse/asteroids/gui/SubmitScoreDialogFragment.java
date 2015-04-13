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

public class SubmitScoreDialogFragment extends DialogFragment {

    private Typeface _spvTypeface;
    private Typeface _basicTypeface;
    private Typeface _squareTypeface;

    private Button _dialogButton;
    private SubmitScoreView _submitScoreView;

    public SubmitScoreDialogFragment() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        _spvTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE_PUSH, getActivity().getAssets());
        _basicTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.BASIC, getActivity().getAssets());
        _squareTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE, getActivity().getAssets());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_submit_score, null);

        TextView dialogTitleTextView = (TextView)dialogView.findViewById(R.id.textViewDialogTitle);
        dialogTitleTextView.setTypeface(_spvTypeface);

        TextView submitTextView = (TextView)dialogView.findViewById(R.id.textViewSubmit);
        submitTextView.setTypeface(_squareTypeface);

        TextView yourScoreTextView = (TextView)dialogView.findViewById(R.id.textViewYourScore);
        yourScoreTextView.setTypeface(_squareTypeface);

        TextView subtitleTextView = (TextView)dialogView.findViewById(R.id.textViewTitle);
        subtitleTextView.setTypeface(_basicTypeface);

        TextView nameSubTitleTextView = (TextView)dialogView.findViewById(R.id.textViewName);
        nameSubTitleTextView.setTypeface(_basicTypeface);

        TextView scoreTextView = (TextView)dialogView.findViewById(R.id.textViewScore);
        scoreTextView.setTypeface(_squareTypeface);
        scoreTextView.setText(Long.toString(SettingsWrapper.getHighScore()));

        String existingName = AsteroidsApplication.getSettings().getName();
        long score = SettingsWrapper.getHighScore();
        _submitScoreView = (SubmitScoreView)dialogView.findViewById(R.id.viewSubmit);
        _submitScoreView.setName(existingName);
        _submitScoreView.setScore(score);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = _submitScoreView.getName();
                AsteroidsApplication.getSettings().setName(name);
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
        //dialog specific code
        //dialog must be fully created and showing for this stuff to work
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

        return dialog;
    }
}
