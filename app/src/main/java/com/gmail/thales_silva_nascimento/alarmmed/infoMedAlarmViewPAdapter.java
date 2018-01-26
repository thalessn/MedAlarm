package com.gmail.thales_silva_nascimento.alarmmed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Thales on 23/01/2018.
 */

public class infoMedAlarmViewPAdapter extends FragmentPagerAdapter {

    public infoMedAlarmViewPAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new fragInfoMedAlarme();
        return fragment;
    }
}
