package com.gmail.thales_silva_nascimento.alarmmed;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;


public class InfoMedAlarmes extends AppCompatActivity {

    TabLayout tabLayout;
    ImageView imgBarra;
    Toolbar toolbar;
    ViewPager viewPager;
    infoMedAlarmViewPAdapter infoMedAlarmViewPAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_med_alarmes);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.tbInfoMedAlarme);
        toolbar.setTitle("Manhã");
        toolbar.setNavigationIcon(R.drawable.ic_menu_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //ImageView
        imgBarra = (ImageView) findViewById(R.id.imgBarra);

        //Tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Manhã"));
        tabLayout.addTab(tabLayout.newTab().setText("Tarde"));
        tabLayout.addTab(tabLayout.newTab().setText("Noite"));
        tabLayout.addTab(tabLayout.newTab().setText("Madrugada"));

        //ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPagerInfoMed);
        infoMedAlarmViewPAdapter = new infoMedAlarmViewPAdapter(getSupportFragmentManager());
        viewPager.setAdapter(infoMedAlarmViewPAdapter);

    }
}
