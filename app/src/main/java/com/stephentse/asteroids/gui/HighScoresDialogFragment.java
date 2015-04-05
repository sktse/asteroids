package com.stephentse.asteroids.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.stephentse.asteroids.R;
import com.stephentse.asteroids.api.crashlytics.CrashlyticsWrapper;
import com.stephentse.asteroids.api.openshift.ApiResponseHandler;
import com.stephentse.asteroids.api.openshift.ICaller;
import com.stephentse.asteroids.api.openshift.InnerApiResponseHandler;
import com.stephentse.asteroids.api.openshift.OpenshiftServerApi;
import com.stephentse.asteroids.util.TypefaceFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HighScoresDialogFragment extends DialogFragment {

    public interface OnDismissListener {
        public void onDismiss();
    }

    private View _progressLayout;
    private View _scoresLayout;
    private TableLayout _scoresTableLayout;
    private Typeface _squareTypeface;
    private Typeface _spvTypeface;

    private OnDismissListener _listener;

    public HighScoresDialogFragment() {
        super();
        _listener = null;
    }

    public void setOnDismissListener(OnDismissListener listener) {
        _listener = listener;
    }

    public OnDismissListener getOnDismissListener() {
        return _listener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (_listener != null) {
            _listener.onDismiss();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        _squareTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE, getActivity().getAssets());
        _spvTypeface = TypefaceFactory.getTypeFace(TypefaceFactory.TypefaceName.SQUARE_PUSH, getActivity().getAssets());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_high_scores, null);

        TextView titleTextView = (TextView)dialogView.findViewById(R.id.textViewTitle);
        titleTextView.setTypeface(_spvTypeface);

        _progressLayout = dialogView.findViewById(R.id.layoutProgress);
        _progressLayout.setVisibility(View.VISIBLE);

        _scoresLayout = dialogView.findViewById(R.id.layoutScores);
        _scoresLayout.setVisibility(View.GONE);

        _scoresTableLayout = (TableLayout)dialogView.findViewById(R.id.tableLayoutScores);

        final int numberOfHighScores = getResources().getInteger(R.integer.number_of_high_scores);

        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                OpenshiftServerApi.getApi().getScores(numberOfHighScores, new ApiResponseHandler(
                        new Caller(dialog),
                        new InnerApiResponseHandler() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                try {
                                    JSONArray scores = result.getJSONArray("scores");
                                    populateScores(_scoresTableLayout, scores);
                                } catch (JSONException e) {
                                    CrashlyticsWrapper.logException("Failed to parse high score results", result, e);
                                    return;
                                }

                                _progressLayout.setVisibility(View.GONE);
                                _scoresLayout.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Throwable e, JSONObject response) {
                                CrashlyticsWrapper.logException("Failed to high score from server", response, e);
                            }

                            @Override
                            public void onFailure(Throwable e) {
                                CrashlyticsWrapper.logException("Failed to high score from server", e);
                            }
                        }
                ));
            }
        });
        dialog.show();
        return dialog;
    }

    private void populateScores(TableLayout tableLayout, JSONArray scores) throws JSONException {
        Context context = getActivity();
        int sideMargins = getResources().getInteger(R.integer.high_score_side_row_margins);
        int topMargins = getResources().getInteger(R.integer.high_score_top_row_margins);
        for (int i = 0; i < scores.length(); i++) {
            JSONObject scoreInfo = scores.getJSONObject(i);

            String name = scoreInfo.getString("name");
            long score = scoreInfo.getLong("score");

            TableRow.LayoutParams tableRowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow row = new TableRow(context);
            row.setLayoutParams(tableRowLayoutParams);

            TableRow.LayoutParams nameTableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            nameTableRowParams.setMargins(sideMargins, topMargins, 0, 0);
            TextView nameTextView = new TextView(context);
            nameTextView.setLayoutParams(nameTableRowParams);
            nameTextView.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            nameTextView.setTypeface(_squareTypeface);
            nameTextView.setTextColor(Color.LTGRAY);
            nameTextView.setText(name);

            TableRow.LayoutParams scoreTableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            scoreTableRowParams.setMargins(0, topMargins, sideMargins, 0);
            TextView scoreTextView = new TextView(context);
            scoreTextView.setLayoutParams(scoreTableRowParams);
            scoreTextView.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            scoreTextView.setTypeface(_squareTypeface);
            scoreTextView.setText(Long.toString(score));
            scoreTextView.setTextColor(Color.WHITE);
            scoreTextView.setGravity(Gravity.RIGHT);

            row.addView(nameTextView);
            row.addView(scoreTextView);
            tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private class Caller implements ICaller {
        private Dialog _dialog;

        public Caller(Dialog dialog) {
            _dialog = dialog;
        }

        @Override
        public boolean active() {
            return _dialog.isShowing();
        }
    }

}
