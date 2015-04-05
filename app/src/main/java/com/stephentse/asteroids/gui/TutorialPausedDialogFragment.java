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

import com.stephentse.asteroids.R;
import com.stephentse.asteroids.util.TypefaceFactory;

public class TutorialPausedDialogFragment extends DialogFragment {

    public interface OnDialogDismissedListener {
        public void onDismiss(int event);
    }

    public static final int QUIT_EVENT = 0;
    public static final int RESUME_EVENT = 1;

    private OnDialogDismissedListener _listener;

    public TutorialPausedDialogFragment() {
        super();
        _listener = null;
    }

    public void setOnDialogDismissedListener(OnDialogDismissedListener listener) {
        _listener = listener;
    }

    public OnDialogDismissedListener getOnDialogDismissedListener() {
        return _listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Typeface spvTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE_PUSH, getActivity().getAssets());
        Typeface basicTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.BASIC, getActivity().getAssets());

        View dialogView = inflater.inflate(R.layout.dialog_tutorial_pause, null);
        TextView titleTextView = (TextView)dialogView.findViewById(R.id.textViewGameOverTitle);
        titleTextView.setTypeface(spvTypeface);

        Button quitButton = (Button)dialogView.findViewById(R.id.buttonQuit);
        quitButton.setTypeface(basicTypeface);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_listener != null) {
                    _listener.onDismiss(QUIT_EVENT);
                }
            }
        });

        Button resumeButton = (Button)dialogView.findViewById(R.id.buttonResume);
        resumeButton.setTypeface(basicTypeface);
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_listener != null) {
                    _listener.onDismiss(RESUME_EVENT);
                }
            }
        });

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
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
        return dialog;
    }
}