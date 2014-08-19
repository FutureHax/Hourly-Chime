package com.t3hh4xx0r.hourlychime;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;


public class TimePickerActivity extends FragmentActivity {
    TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private int[] quietHours = new int[4];

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Calendar startCalendar = Calendar.getInstance();
        startTimePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
                final Calendar endCalendar = Calendar.getInstance();
                quietHours[0] = hourOfDay;
                quietHours[1] = minute;

                endCalendar.set(Calendar.MINUTE, minute);
                endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                endTimePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
                        //done, finish
                        quietHours[2] = hourOfDay;
                        quietHours[3] = minute;
                        new SettingsProvider(radialPickerLayout.getContext()).setQuietHours(quietHours);
                        finish();
                    }
                }, endCalendar.get(Calendar.HOUR_OF_DAY), endCalendar.get(Calendar.MINUTE), false, true, "End Time");
                endTimePickerDialog.show(getSupportFragmentManager(), "tag");
            }
        }, startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.MINUTE), false, true, "Start Time");
        startTimePickerDialog.show(getSupportFragmentManager(), "tag");
    }
}
