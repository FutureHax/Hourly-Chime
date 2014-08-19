package com.t3hh4xx0r.hourlychime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {

    /**
     * Determines whether to always show the simplified settings UI, where
     * settings are presented in a single list. When false, settings are shown
     * as a master/detail two-pane view on tablets. When true, a single pane is
     * shown on tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = false;
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            if (preference.getKey().equals("enable_mobile")) {
                boolean booleanValue = Boolean.parseBoolean(value.toString());
                preference.setSummary(booleanValue ? "Enabled on device" : "Disabled on device");
            } else if (preference.getKey().equals("enable_wear")) {
                boolean booleanValue = Boolean.parseBoolean(value.toString());
                preference.setSummary(booleanValue ? "Enabled on wear" : "Disabled on wear");
            }

//            else if (preference.getKey().equals("pattern")) {
//                Log.d("THE PATTERN VALUE", value.toString());
//                preference.setSummary("");
//            } else if (preference.getKey().equals("simple_pattern")) {
//                Log.d("THE SIMPLE PATTERN VALUE", value.toString());
//                preference.setSummary("");
//            }
            return true;
        }
    };
    DateTimeFormatter fmt = DateTimeFormat.forPattern("dd HH:mm");
    String PLAY_LINK = "https://play.google.com/store/apps/details?id=com.t3hh4xx0r.hourlychime";
    Vibrator vibe;
    DecimalFormat df = new DecimalFormat("00");

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Determines whether the simplified settings UI should be shown. This is
     * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
     * doesn't have newer APIs like {@link PreferenceFragment}, or the device
     * doesn't have an extra-large screen. In these cases, a single-pane
     * "simplified" settings UI should be shown.
     */
    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        if (preference.getKey().equals("enable_mobile")) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getBoolean(preference.getKey(), true)
            );
        } else if (preference.getKey().equals("enable_wear")) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getBoolean(preference.getKey(), true)
            );
        }

//        else if (preference.getKey().equals("simple_pattern")) {
//            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
//                    PreferenceManager
//                            .getDefaultSharedPreferences(preference.getContext())
//                            .getInt(preference.getKey(), -1)
//            );
//        } else if (preference.getKey().equals("pattern")) {
//            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
//                    PreferenceManager
//                            .getDefaultSharedPreferences(preference.getContext())
//                            .getString(preference.getKey(), "[]")
//            );
//        }


    }

    public void setQuietHoursSummary() {
        int[] quietHours = new SettingsProvider(this).getQuietHours();
        Log.d("THE LENGTH", quietHours.length + ":");
        if (quietHours.length == 4) {
            StringBuilder sb = new StringBuilder();
            int hourStart = quietHours[0];
            if (hourStart > 12) {
                sb.append(df.format(hourStart - 12));
            } else {
                sb.append(df.format(hourStart));
            }
            sb.append(":");
            sb.append(df.format(quietHours[1]));
            if (hourStart > 12) {
                sb.append("pm");
            } else {
                sb.append("am");
            }
            sb.append(" - ");
            int hourEnd = quietHours[2];
            if (hourEnd > 12) {
                sb.append(df.format(hourEnd - 12));
            } else {
                sb.append(df.format(hourEnd));
            }
            sb.append(":");
            sb.append(df.format(quietHours[3]));
            if (hourEnd > 12) {
                sb.append("pm");
            } else {
                sb.append("am");
            }
            findPreference("quiet_hours").setSummary(sb.toString());
        } else {
            findPreference("quiet_hours").setSummary("Quiet hours not set.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int id, MenuItem item) {
        int ID = item.getItemId();

        if (ID == R.id.menu_contact) {
            showFeedbackChooser();
        }
        return super.onMenuItemSelected(id, item);
    }

    public void showFeedbackChooser() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setPositiveButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                String sAux = "Check out this amazing application! ";
                sAux = sAux
                        + PLAY_LINK + " \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Share via..."));
            }
        });
        b.setNegativeButton("Rate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Intent openIntent = new Intent(
                        Intent.ACTION_VIEW);
                openIntent.setData(Uri.parse(PLAY_LINK));
                startActivity(openIntent);
            }
        });
        b.setNeutralButton("Send Feedback", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Intent emailIntent = new Intent(
                        android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[]{"r2doesinc@futurehax.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Hourly Chime Feedback");
                startActivity(Intent.createChooser(emailIntent, "Send via..."));
            }
        });
        b.create().show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        startService(new Intent(this, HourlyChimeService.class));
        vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        setupSimplePreferencesScreen();
//        runTimeTests();

//        Preference debug = findPreference("debug");
//        debug.setWidgetLayoutResource(R.layout.widget_button);
//        debug.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
//                return false;
//            }
//        });

        findPreference("quiet_hours").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                AlertDialog.Builder b = new AlertDialog.Builder(preference.getContext());
                b.setNegativeButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new SettingsProvider(preference.getContext()).clearQuietHours();
                        setQuietHoursSummary();
                    }
                });
                b.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(preference.getContext(), TimePickerActivity.class));
                    }
                });
                b.show();
                return false;
            }
        });

        findPreference("simple_pattern").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                final SettingsProvider settings = new SettingsProvider(preference.getContext());
                View root = LayoutInflater.from(preference.getContext()).inflate(R.layout.simlple_pattern_selector, null, false);
                final RadioGroup rG = (RadioGroup) root.findViewById(R.id.simple_options);
                rG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.pattern_long:
                                vibe.vibrate(1000);
                                break;

                            case R.id.pattern_short:
                                vibe.vibrate(250);
                                break;

                            case R.id.pattern_medium:
                                vibe.vibrate(500);
                                break;
                        }
                    }
                });
                AlertDialog.Builder b = new AlertDialog.Builder(preference.getContext());
                b.setTitle("Vibration Length").setPositiveButton("Set", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (rG.getCheckedRadioButtonId()) {
                            case R.id.pattern_long:
                                settings.setSimpleVibrationPattern(1000);
                                break;

                            case R.id.pattern_short:
                                settings.setSimpleVibrationPattern(250);
                                break;

                            case R.id.pattern_medium:
                                settings.setSimpleVibrationPattern(500);
                                break;
                        }

                        setSimpleSummary(settings);
                        setComplexSummary(settings);
                    }
                }).setNegativeButton("Cancel", null).setView(root);
                b.show();
                return false;
            }
        });
    }

    void testTime(int hour) {
        DateTime now = new DateTime(Calendar.getInstance()).withHourOfDay(hour).withMinuteOfHour(0);
        int[] quietHours = new int[]{23, 0, 8, 0};
        DateTime start = new DateTime().withHourOfDay(quietHours[0]).withMinuteOfHour(quietHours[1]);
        DateTime end = new DateTime().withHourOfDay(quietHours[2]).withMinuteOfHour(quietHours[3]);
        if (end.isBefore(start)) {
            end = end.withFieldAdded(DurationFieldType.days(), 1);
        }
        if (now.getHourOfDay() <= end.getHourOfDay()) {
            now = now.withFieldAdded(DurationFieldType.days(), 1);
        }
        boolean isInQuietHours = new Interval(start, end).contains(now);

        Log.d("MY TEST RESULT", fmt.print(now) + " is between " + fmt.print(start) + " and " + fmt.print(end) + " is : " + Boolean.toString(isInQuietHours));

    }

    private void runTimeTests() {
       for (int i=0;i<23;i++) {
           testTime(i);
       }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSimpleSummary(new SettingsProvider(this));
        setComplexSummary(new SettingsProvider(this));
        setQuietHoursSummary();
    }

    public void setSimpleSummary(SettingsProvider settings) {
        Preference simpleVibePref = findPreference("simple_pattern");
        if (settings.getSimpleVibrationPattern() == 500) {
            simpleVibePref.setSummary("Half a second");
        } else if (settings.getSimpleVibrationPattern() == 250) {
            simpleVibePref.setSummary("Quarter of a second");
        } else if (settings.getSimpleVibrationPattern() == 1000) {
            simpleVibePref.setSummary("One second");
        } else {
            simpleVibePref.setSummary("Custom pattern set");
        }
    }

    public void setComplexSummary(SettingsProvider settings) {
        Preference simpleVibePref = findPreference("pattern");
        if (settings.getSimpleVibrationPattern() == -1) {
            simpleVibePref.setSummary("Custom pattern set");
        } else {
            simpleVibePref.setSummary("No custom pattern set");
        }
    }


    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }

        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference("enable_wear"));
        bindPreferenceSummaryToValue(findPreference("enable_mobile"));

//        bindPreferenceSummaryToValue(findPreference("pattern"));
//        bindPreferenceSummaryToValue(findPreference("simple_pattern"));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }
}
