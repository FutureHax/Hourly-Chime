package com.t3hh4xx0r.hourlychime;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by r2doesinc on 7/22/14.
 *
 * Custom {@link android.preference.Preference} including a clickable {@link android.widget.Button}
 *
 */
public class ButtonPreference extends Preference {

    public ButtonPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.widget_button);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNonButtonClick(v);
            }
        });
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });
    }

    /**
     * Occurs when inner button was clicked.
     *
     * @param v
     */
    public void onButtonClick(View v) {
        Vibrator vibe = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);

        SettingsProvider s = new SettingsProvider(v.getContext());
        if (s.getSimpleVibrationPattern() != -1) {
            vibe.vibrate(s.getSimpleVibrationPattern());
        } else {
            vibe.vibrate(s.getCustomVibrationPattern(), -1);
        }
    }

    /**
     * Occurs when the root preference was clicked
     *
     * @param v
     */
    private void onNonButtonClick(View v) {
        v.getContext().startActivity(new Intent(v.getContext(), SetPatternActivity.class));
    }
}
