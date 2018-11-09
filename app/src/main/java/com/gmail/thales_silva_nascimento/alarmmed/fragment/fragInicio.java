package com.gmail.thales_silva_nascimento.alarmmed.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.activity.MainActivity;
import com.gmail.thales_silva_nascimento.alarmmed.adapter.homeViewPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragInicio extends Fragment {

    //ViewPage do fragmento inicial
    private ViewPager viewPager;
    private homeViewPagerAdapter adapter;



    public fragInicio() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_inicio, container, false);

        //Altera o título da toolbar
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Med Alarm");

        /**
         * ViewPager
         */
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        //Usando getChildFragment funciona a transição do DrawerLayout
        adapter = new homeViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(5);

        Log.v("FragInicio", "ViewPager");


        return view;
    }

}
