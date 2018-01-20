package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.dao.BancoHelper;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.fragRemedio;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.fragAlarme;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.fragCompra;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.fragInicio;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.fragMedico;
import com.gmail.thales_silva_nascimento.alarmmed.fragment.fragReceita;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean menu = true;
    private NavigationView navigationView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicializa o Banco de dados para criar tabelas do banco
        BancoHelper banco = BancoHelper.getInstance(MainActivity.this);

        //Adiciona a ToolBar
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("AlarmMed");

        //Adiciona o evento onlick no Floating Button (Botão mais da Tela)
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cria -se um objeto fragment para saber qual fragment está sendo mostrado ao usuário
                Fragment currentfragment = getSupportFragmentManager().findFragmentById(R.id.content_main);
                //A tag do Frament atual
                String tag = currentfragment.getTag();
                Log.v("Tag", tag);
                switch (tag){
                    case "fragReceita":
                        Intent receita = new Intent(view.getContext(), receitaCadastro.class);
                        startActivity(receita);
                        break;
                    default:
                        break;
                }
            }
        });


        //Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Encontra a view do navigation drawer e set o evento onclick item
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Adiciona o evento Onclick no Header do Navigatio Drawer
        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(menu){
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.activity_main_drawer2);
                    menu = false;
                }else{
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.activity_main_drawer);
                    menu = true;
                }

            }
        });

        //Seta o Fragment fragInicio no content_main do Navigation Drawer
        Fragment inicialFrag = new fragInicio();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, inicialFrag, "fragInicio");
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
        } else {
            //super.onBackPressed();
            //Verifica se o fragment atual é o inicio se for então pode encerrar a aplicação. Se não volte para o inicio
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_main);
            if(f.getClass().getName().equals(fragInicio.class.getName())){ // here HomeFragment.class.getName() means from which faragment you actually want to exit
                finish();
            }else{
                displaySelectedScreen(R.id.nav_inicio);
            }
        }
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Função criada para gerenciar qual fragment deve ser exibido quando o usuário selecionar no NavigationDrawer
     * Tem como parâmetro id do menu selecionado pelo usuário
     * @param id
     */
    private void displaySelectedScreen(int id){
        Fragment fragment = null;
        String fragmentTag = null;
        switch (id){
            case R.id.nav_inicio:
                floatingButtonShow(true);
                fragment = new fragInicio();
                fragmentTag = "fragInicio";
                break;
            case R.id.nav_medico:
                floatingButtonShow(false);
                fragment = new fragMedico();
                fragmentTag = "fragMedico";
                break;
            case R.id.nav_remedios:
                floatingButtonShow(false);
                fragment = new fragRemedio();
                fragmentTag = "fragRemedio";
                break;
            case R.id.nav_receita:
                floatingButtonShow(true);
                fragment = new fragReceita();
                fragmentTag = "fragReceita";
                break;
            case R.id.nav_compra:
                floatingButtonShow(false);
                fragment = new fragCompra();
                fragmentTag = "fragCompra";
                break;
            case R.id.nav_alarme:
                floatingButtonShow(false);
                fragment = new fragAlarme();
                fragmentTag = "fragAlarme";
                break;
            case R.id.nav_edPerfil:
                floatingButtonShow(false);
                Intent i = new Intent(MainActivity.this, editaPerfil.class);
                startActivity(i);
                Toast.makeText(MainActivity.this, "Editar Perfil", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_addPerfil:
                floatingButtonShow(false);
                Intent intent = new Intent(MainActivity.this, CriaPerfl.class);
                startActivity(intent);
                break;
        }
        if(fragment !=null && fragmentTag != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment, fragmentTag);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    /**
     * Função que diz qual item o usuário selecionou no menu
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }

    /**
     * Função para deixar invisível o FloatingButton nos Fragments específicos
     * @param show
     */
    private void floatingButtonShow(boolean show){
        if(show){
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.INVISIBLE);
        }

    }
}
