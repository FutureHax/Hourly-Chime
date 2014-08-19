package com.t3hh4xx0r.hourlychime;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by r2doesinc on 6/30/14.
 */
public class SettingsProvider {
    Context context;
    SharedPreferences p;

    public SettingsProvider(Context context) {
        this.context = context;
        p = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getIsEnabledOnWear() {
        return p.getBoolean("enable_wear", true);
    }

    public boolean getIsEnabledOnDevice() {
        return p.getBoolean("enable_mobile", true);
    }

    public long getSimpleVibrationPattern() {
        return p.getInt("simple_pattern", 250);
    }

    public void setSimpleVibrationPattern(long time) {
        p.edit().putInt("simple_pattern", Long.valueOf(time).intValue()).commit();
        p.edit().putString("pattern", "[]").commit();
    }

    public long[] getCustomVibrationPattern() {
        String s = p.getString("pattern", "[]");
        List<String> list = Arrays.asList(s.substring(1, s.length() - 1).split(", "));
        long[] res = new long[list.size() + 1];
        res[0] = 0;
        for (int i = 0; i < list.size(); i++) {
            res[i + 1] = Long.parseLong(list.get(i));
        }
        return res;
    }

    public void setCustomVibrationPattern(Long[] pattern) {
        p.edit().putString("pattern", Arrays.toString(pattern)).commit();
        p.edit().putInt("simple_pattern", -1).commit();

    }

    public int[] getQuietHours() {
        String s = p.getString("quiet_hours", "[]");
        List<String> list = Arrays.asList(s.substring(1, s.length() - 1).split(", "));
        int[] res = new int[list.size()];
        if (!list.get(0).isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                res[i] = Integer.parseInt(list.get(i));
            }
        }
        return res;
    }

    public void setQuietHours(int[] hours) {
        p.edit().putString("quiet_hours", Arrays.toString(hours)).commit();
    }

    public void clearQuietHours() {
        p.edit().putString("quiet_hours", "[]").commit();
    }
}

