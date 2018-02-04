package com.gmail.thales_silva_nascimento.alarmmed;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


public class InfoMedAlarmes extends AppCompatActivity {

    private TabLayout tabLayout;
    private ImageView imgBarra;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private infoMedAlarmViewPAdapter infoMedAlarmViewPAdapter;
    private int itemGridPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_med_alarmes);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.tbInfoMedAlarme);
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

        //Posicao do viewpager
        final int posicao = getIntent().getExtras().getInt("posicao");

        //Determina qual Item da Grid (Manhã - Tarde - Noite - Madrugada) foi selecionado
        itemGridPosition = getIntent().getExtras().getInt("ItemGridPosition");

        //Determina qual titulo deve ser usado em relação ao itemGridPositio
        setupTitleToolbar(itemGridPosition);

        //ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPagerInfoMed);
        infoMedAlarmViewPAdapter = new infoMedAlarmViewPAdapter(getSupportFragmentManager(), posicao);
        viewPager.setAdapter(infoMedAlarmViewPAdapter);

        viewPager.setCurrentItem(itemGridPosition);

        //Muda o titulo na toolbar de acordo com a seleção da página
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        getSupportActionBar().setTitle("Manhã");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Tarde");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Noite");
                        break;
                    case 3:
                        getSupportActionBar().setTitle("Madrugada");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Manhã"));
        tabLayout.addTab(tabLayout.newTab().setText("Tarde"));
        tabLayout.addTab(tabLayout.newTab().setText("Noite"));
        tabLayout.addTab(tabLayout.newTab().setText("Madrugada"));

        tabLayout.setupWithViewPager(viewPager);

    }

    public void setupTitleToolbar(int itemGridPosition){
        switch (itemGridPosition){
            case 0:
                getSupportActionBar().setTitle("Manhã");
                break;
            case 1:
                getSupportActionBar().setTitle("Tarde");
                break;
            case 2:
                getSupportActionBar().setTitle("Noite");;
                break;
            case 3:
                getSupportActionBar().setTitle("Madrugada");
                break;
        }
    }
}
