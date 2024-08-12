package com.wiame.monzoo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnimalActivity extends Activity implements View.OnClickListener {

    // recoit les evenements venant de notre thread
    private Handler mainHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal);
        Button btEnvoi = findViewById(R.id.button);
        btEnvoi.setOnClickListener(this);
        // quand on lui enverra un evenement il executera le code
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onClick(View v) {
        EditText etIntitule = findViewById(R.id.etNom);
        String nom = etIntitule.getText().toString();

        EditText etAge = findViewById(R.id.etAge);
        int age = Integer.parseInt(etAge.getText().toString());

        EditText etEspece = findViewById(R.id.etEspece);
        String espece = etEspece.getText().toString();

        if(v.getId() == R.id.button) {
            if (!nom.isEmpty()) {
                Log.i("AnimalActivity", "Animal ajouté : " + nom);
            }

            Log.i("AnimalActivity", "Age ajouté : " + age);

            if (!espece.isEmpty()) {
                Log.e("AnimalActivity", "Espece ajouté : " + espece);
                Toast.makeText(this, getString(R.string.animal_valide), Toast.LENGTH_LONG).show();
            }

            // DAO
            ZooHelper zooHelper = new ZooHelper(this);
            int nb = zooHelper.insererAnimal(nom, age, espece);
            Log.i("AnimalActivity", "C'est l'espece " + espece + " numero : " + nb);

            List<String> animaux = zooHelper.getAnimal();
            for (String animal : animaux) {
                Log.i("AnimalActivity", animal);
            }

            etEspece.setText("");
            etAge.setText("");
            etIntitule.setText("");
        }

        if (v.getId() == R.id.buttonChercher){
            // test des permissions, si elle est autorisee
            if(checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                // Construit 2 Threads en reserve qui sont en attente
                ExecutorService executorService = Executors.newFixedThreadPool(2);
                // a la place d'appeler directement la methode chercher() = on lui demande de l'executer dans un thread secondaire (l'un des 2 créé précdement)
                // this:: revient a faire une fonction lambda
                executorService.execute(this::chercher);
            } else {
                // demande a l'usager d'avoir une permission
                requestPermissions(new String[]{
                        Manifest.permission.INTERNET
                }, 0);
            }
        }
    }

    // sera appelée quand l'usager aura appuiyé sur oui ou non
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            // Construit 2 Threads en reserve qui sont en attente
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            executorService.execute(this::chercher);
        } else {
            Log.i("AnimalActivity", "L'usager refuse l'accès à internet");
        }
    }

    public void chercher(){
        try {
            afficher("...");
            EditText etEspece = findViewById(R.id.etEspece);
            String espece1 = etEspece.getText().toString();
            // encodage de l'url pour pouvoir accepter les espaces et autres caracteres speciaux
            String url = "https://fr.wikipedia.org/w/api.php?action=query&prop=extracts&exsentences=3&format=json&titles="+ URLEncoder.encode(espece1, "UTF-8");
            Log.i("AnimalActivity", "Recherche de : "+url);

            URLConnection connection = new URL(url).openConnection();
            InputStream is = connection.getInputStream();
            // Lire le flux, ecrire dans la TextView
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String contenuJSON = "", ligne;
            while ((ligne = reader.readLine()) != null) {
                contenuJSON += ligne;
            }
            reader.close();
            is.close();

            //afficher le contenu (ici l'html) qui nous interesse a partir du JSON
            JSONObject racine = new JSONObject(contenuJSON);
            JSONObject query = racine.getJSONObject("query");
            JSONObject pages = query.getJSONObject("pages");
            String numeroPage = pages.keys().next();
            JSONObject page = pages.getJSONObject(numeroPage);
            String contenuHtml = page.getString("extract");

            // formater le code html avec ce qu'il donne reellement
            afficher(Html.fromHtml(contenuHtml));
        } catch (Exception e) {
            Log.i("AnimalActivity", "Echec recherche", e);
        }
    }

    private void afficher(CharSequence message){
        // post = envoie le plus tot possible dans le thread principal
        mainHandler.post(() -> {
            TextView tvResultat = findViewById(R.id.tvResultat);
            tvResultat.setText(message);
        });
    }
}

