package com.gmail.thales_silva_nascimento.alarmmed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Thales on 21/01/2018.
 */

public class homeViewPagerAdapter extends FragmentPagerAdapter {
    public static int NUM_ITEMS = 11;

    public homeViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragHome.newInstance(position);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
