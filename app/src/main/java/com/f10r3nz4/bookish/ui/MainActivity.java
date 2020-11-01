package com.f10r3nz4.bookish.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.f10r3nz4.bookish.R;
import com.f10r3nz4.bookish.Ricerca.ClassiciActivity;
import com.f10r3nz4.bookish.Ricerca.FantasyActivity;
import com.f10r3nz4.bookish.Ricerca.GialliActivity;
import com.f10r3nz4.bookish.Ricerca.HorrorActivity;
import com.f10r3nz4.bookish.Ricerca.NarrativaActivity;
import com.f10r3nz4.bookish.Ricerca.PerRagazziActivity;
import com.f10r3nz4.bookish.Ricerca.RicercaAutoreActivity;
import com.f10r3nz4.bookish.Ricerca.RicercaTitoloActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    //sfoglia
    private ImageButton giallo, narrativa, perRagazzi, horror, classici, fantasy;
    private TextView perTitolo, perAutore, txtAggiungi, txtGiallo, txtNarrativa, txtPerRagazzi, txtHorror, txtClassici, txtFantasy;
    private ImageView cerca1, cerca2, aggiungi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //menu
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //sfoglia
        txtAggiungi = findViewById(R.id.textAggiungi);
        perTitolo = findViewById(R.id.textCercaTit);
        perAutore = findViewById(R.id.textCercaAut);
        giallo = findViewById(R.id.btnGiallo);
        narrativa = findViewById(R.id.btnNarrativa);
        perRagazzi = findViewById(R.id.btnPerRagazzi);
        horror = findViewById(R.id.btnHorror);
        fantasy = findViewById(R.id.btnFantasy);
        classici = findViewById(R.id.btnClassici);
        cerca1 = findViewById(R.id.imageCerca1);
        cerca2 = findViewById(R.id.imageCerca2);
        aggiungi = findViewById(R.id.imageAggiungi);
        txtClassici = findViewById(R.id.textClassici);
        txtGiallo = findViewById(R.id.textGiallo);
        txtNarrativa = findViewById(R.id.textNarrativa);
        txtPerRagazzi = findViewById(R.id.textPerRagazzi);
        txtHorror = findViewById(R.id.textHorror);
        txtFantasy = findViewById(R.id.textFantasy);

        txtAggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Se non trovi il libro che stai cercando, aggiungilo!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), NuovoLibroActivity.class));
            }
        });

        perTitolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RicercaTitoloActivity.class));
            }
        });

        perAutore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RicercaAutoreActivity.class));
            }
        });

        giallo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GialliActivity.class));
            }
        });

        narrativa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NarrativaActivity.class));
            }
        });

        perRagazzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PerRagazziActivity.class));
            }
        });

        horror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HorrorActivity.class));
            }
        });

        fantasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FantasyActivity.class));
            }
        });

        classici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ClassiciActivity.class));
            }
        });


        //menu
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_sfoglia:
                        break;
                    case R.id.nav_letture:
                        startActivity(new Intent(getApplicationContext(), LettureActivity.class));
                        break;
                    case R.id.nav_libri:
                        startActivity(new Intent(getApplicationContext(), LibriActivity.class));
                        break;
                    case R.id.nav_esci:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        navigationView.setCheckedItem(R.id.nav_sfoglia);
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

}
