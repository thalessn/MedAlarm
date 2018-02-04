package com.gmail.thales_silva_nascimento.alarmmed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by Thales on 23/01/2018.
 */

public class infoMedAlarmViewPAdapter extends FragmentStatePagerAdapter {
    private int posicao;
    private int itemGridPosition;
    public infoMedAlarmViewPAdapter(FragmentManager fm, int posicao){
        super(fm);
        this.posicao = posicao;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new fragInfoMedAlarme();
        Bundle args = new Bundle();
        args.putInt("position", posicao);
        //Escolhe o valor de itemGridPosition em relação a posicao
        getItemGridPosition(position);
        args.putInt("ItemGridPosition", itemGridPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position){
            case 0:
                return title = "manhã";
            case 1:
                return  title  ="tarde";
            case 2:
                return  title = "noite";
            case 3:
                return "madrugada";

            default:
                return title = "manhã";
        }
    }

    /**
     * Determina o valor de ItemGridPosition em relação a posição da página escolhida.
     * O que determina o parametro de pesquisa no fragInfoMedAlarme.
     * @param position
     * @return
     */
    public int getItemGridPosition(int position){
        switch (position){
            case 0:
                return itemGridPosition = 0;
            case 1:
                return itemGridPosition = 1;
            case 2:
                return itemGridPosition = 2;
            case 3:
                return itemGridPosition = 3;
        }
        return 0;
    }

}
