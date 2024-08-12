package com.wiame.monzoo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AccueilActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        lireNouvelles();
        Button btCarte = findViewById(R.id.btCarte);
        btCarte.setOnClickListener(this);
        Button btAlerte = findViewById(R.id.btAlerte);
        btAlerte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccueilActivity.this, AlerteActivity.class);
                startActivity(i);
            }
        });

        Button btAnnuaire = findViewById(R.id.btAnnuaire);
        btAnnuaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccueilActivity.this, AnnuaireActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, CarteActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        // allocation de memoire
        SharedPreferences sp = getSharedPreferences("zoo", MODE_PRIVATE);
        menu.findItem(R.id.menu_envoyer).setChecked(sp.getBoolean("envoyer", true));
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, @NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_carte){
            startActivity(new Intent(this, CarteActivity.class));
        }
        if(item.getItemId()==R.id.menu_animal){
            startActivity(new Intent(this, AnimalActivity.class));
        }
        if(item.getItemId()==R.id.menu_envoyer){
            item.setChecked(!item.isChecked());

            // stockage dans un fichier interne
            SharedPreferences sp = getSharedPreferences("zoo", MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("envoyer", item.isChecked());
            e.commit();
        }
        return true;
    }

    private void lireNouvelles() {
            StringBuilder contenu = new StringBuilder();
            try {
                InputStream is = getAssets().open("news.txt");
                // Utiliser un StringBuilder pour accumuler le contenu du fichier
                // Lire le fichier ligne par ligne
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    contenu.append(line).append('\n');
                }
                reader.close();
            } catch (IOException e) {
                Log.e("AccueilActivity", "Erreur lecture news", e);
            }
            TextView tvNouvelles = findViewById(R.id.tvNouvelles);
            tvNouvelles.setText(contenu.toString());
        }
}

