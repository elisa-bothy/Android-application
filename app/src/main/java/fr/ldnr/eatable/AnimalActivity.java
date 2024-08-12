package fr.ldnr.eatable;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnimalActivity extends Activity implements View.OnClickListener {

    private Handler mainHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal);
        Button buttonAnimal = findViewById(R.id.btAnimal);
        buttonAnimal.setOnClickListener(this);

        String[] animaux = getResources().getStringArray(R.array.list_animaux);
        AutoCompleteTextView etAnimaux = findViewById(R.id.et_animal_espece);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, animaux);
        etAnimaux.setAdapter(aa);

        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onClick(View v) {
        EditText etNomAnimal = findViewById(R.id.et_animal_nom);
        String nom = etNomAnimal.getText().toString();

        EditText etTypeAnimal = findViewById(R.id.et_animal_espece);
        String espece = etTypeAnimal.getText().toString();

        EditText etAgeAnimal = findViewById(R.id.et_animal_age);
        int age = Integer.parseInt(etAgeAnimal.getText().toString());

        if(etNomAnimal.getText().toString().isEmpty() || etTypeAnimal.getText().toString().isEmpty()
                || etAgeAnimal.getText().toString().isEmpty()){
            Toast.makeText(this, "Veuillez remplir tous les paramètres",
                    Toast.LENGTH_LONG).show();
        }if(v.getId() == R.id.btAnimal) {
            if (!nom.isEmpty()) {
                Log.i("AnimalActivity", "Animal ajouté : " + nom);
            }

            Log.i("AnimalActivity", "Age ajouté : " + age);

            if (!espece.isEmpty()) {
                Log.e("AnimalActivity", "Espece ajouté : " + espece);
                Toast.makeText(this, getString(R.string.animal_valide), Toast.LENGTH_LONG).show();
            }

            OceanHelper oh = new OceanHelper(this);
            int result = oh.insererAnimal(etNomAnimal.getText().toString(),
                    Integer.parseInt(etAgeAnimal.getText().toString()), etTypeAnimal.getText().toString());
            Log.i("AnimalActivity" ,"le resultat est : " + result);
            List<String> animaux = oh.getAnimaux();

            for (String animal: animaux){
                Log.i("AnimalActivity", "L'animal : "+ animal);
            }

            etNomAnimal.setText("");
            etAgeAnimal.setText("");
            etTypeAnimal.setText("");
        }

        if (v.getId() == R.id.buttonChercher){
            // test des permissions, si elle est autorisee
            if(checkSelfPermission(android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
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
            EditText etEspece = findViewById(R.id.et_animal_espece);
            String espece1 = etEspece.getText().toString();
            // encodage de l'url pour pouvoir accepter les espaces et autres caracteres speciaux
            String url = "https://fr.wikipedia.org/w/api.php?action=query&prop=extracts&exsentences" +
                    "=3&format=json&titles="+ URLEncoder.encode(espece1, "UTF-8");
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
