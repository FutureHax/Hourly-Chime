package com.t3hh4xx0r.hourlychime;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.wearable.view.CardFragment;
import android.view.View;

public class CardFragmentPagerAdapter extends FragmentPagerAdapter {
public Activity act;

    public CardFragmentPagerAdapter(Activity act) {
        super(act.getFragmentManager());
        this.act = act;
    }



    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new UberCardFragment();
        } else {
            return new UberFragment();
        }
    }

}