package com.gmail.thales_silva_nascimento.alarmmed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Thales on 23/01/2018.
 */

public class infoMedAlarmViewPAdapter extends FragmentPagerAdapter {
    private int posicao;
    public infoMedAlarmViewPAdapter(FragmentManager fm, int posicao){
        super(fm);
        this.posicao = posicao;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new fragInfoMedAlarme();
        Bundle args = new Bundle();
        args.putInt("position", posicao);
        fragment.setArguments(args);
        return fragment;
    }
}
