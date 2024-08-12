package fr.ldnr.eatable;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class IndexActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        lireNouvelles();
        Button btAlerte = findViewById(R.id.btAlerte);
        btAlerte.setOnClickListener(this);
        Button btAqua = findViewById(R.id.btCarte);
        btAqua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IndexActivity.this, JardinActivity.class);
                startActivity(i);
            }
        });
        Button btAnnuaire = findViewById(R.id.btAnnuaire);
        btAnnuaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IndexActivity.this, AnnuaireActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        SharedPreferences sp =getSharedPreferences("ocean", MODE_PRIVATE);
        menu.findItem(R.id.menu_envoyer).setChecked(sp.getBoolean("envoyer", true));
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, @NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_carte){
            startActivity(new Intent(IndexActivity.this, JardinActivity.class));
        }if(item.getItemId()==R.id.menu_aquarium){
            startActivity(new Intent(IndexActivity.this, AquariumActivity.class));
        }if(item.getItemId()==R.id.menu_carotte){
            startActivity(new Intent(IndexActivity.this, CarotteActivity.class));
        }if(item.getItemId()==R.id.menu_popcorn){
            startActivity(new Intent(IndexActivity.this, PopcornActivity.class));
        }if(item.getItemId()==R.id.menu_animal){
            startActivity(new Intent(IndexActivity.this, AnimalActivity.class));
        }if(item.getItemId()==R.id.menu_envoyer){
            item.setChecked(!item.isChecked());
            SharedPreferences sp =getSharedPreferences("ocean", MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("envoyer", item.isChecked());
            e.apply();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, AlerteActivity.class);
        startActivity(i);
    }

    private void lireNouvelles(){
        try {
            InputStream is = getAssets().open("news.txt");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            String content = sb.toString();

            TextView tvNouvelles = findViewById(R.id.tvNouvelles);
            tvNouvelles.setText(content);

            // Assurez-vous de fermer les flux après les avoir utilisés
            br.close();
            is.close();
        }catch (Exception e){
            Log.e("IndexActivity", "Erreur lors de la lecture de la news");
        }
    }


}
