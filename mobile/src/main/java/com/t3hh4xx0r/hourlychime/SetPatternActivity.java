package com.t3hh4xx0r.hourlychime;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Allow the user to tap out a pattern on the screen to be played back via the vibrator.
 */
public class SetPatternActivity extends Activity {
    Vibrator vibe;
    List<Long> pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pattern);
        pattern = new ArrayList<Long>();
        vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            long start, end = -1, pause;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == android.view.MotionEvent.ACTION_DOWN) {
                    start = System.currentTimeMillis();
                    if (end != -1) {
                        pause = start - end;
                        pattern.add(Long.valueOf(pause));
                    }
                    vibe.vibrate(new long[] {0, 1000}, 0);
                    v.setBackgroundColor(v.getContext().getResources().getColor(android.R.color.holo_blue_bright));
                } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    end = System.currentTimeMillis();
                    vibe.cancel();
                    pattern.add(Long.valueOf(end - start));
                    v.setBackgroundColor(v.getContext().getResources().getColor(android.R.color.white));
                }
                return true;
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        vibe.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.set_pattern, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            new SettingsProvider(this).setCustomVibrationPattern(pattern.toArray(new Long[pattern.size()]));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
