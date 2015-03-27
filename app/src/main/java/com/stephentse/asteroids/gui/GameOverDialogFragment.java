package com.stephentse.asteroids.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.stephentse.asteroids.R;
import com.stephentse.asteroids.util.TypefaceFactory;

public class GameOverDialogFragment extends DialogFragment {

    public interface OnButtonClickListener {
        public void onClick();
    }

    private long _score;
    private OnButtonClickListener _listener;

    public GameOverDialogFragment() {
        super();
        _score = 0;
        _listener = null;
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

        Typeface spvTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE_PUSH, getActivity().getAssets());
        Typeface basicTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.BASIC, getActivity().getAssets());
        Typeface squareTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE, getActivity().getAssets());

        View dialogView = inflater.inflate(R.layout.dialog_gameover, null);
        TextView titleTextView = (TextView)dialogView.findViewById(R.id.textViewTitle);
        titleTextView.setTypeface(spvTypeface);

        TextView subtitleTextView = (TextView)dialogView.findViewById(R.id.textViewSubtitle);
        subtitleTextView.setTypeface(basicTypeface);

        TextView scoreTextView = (TextView)dialogView.findViewById(R.id.textViewScore);
        scoreTextView.setTypeface(squareTypeface);
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
        return dialog;
    }
}
