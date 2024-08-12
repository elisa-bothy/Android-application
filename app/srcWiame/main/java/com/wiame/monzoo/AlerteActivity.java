package com.wiame.monzoo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AlerteActivity extends Activity {
    public static String message = "activité créée";
    private long debut, fin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alerte);
        String[] lieux = getResources().getStringArray(R.array.alerte_lieux);
        AutoCompleteTextView etLieu = findViewById(R.id.Lieu);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, lieux);
        etLieu.setAdapter(aa);
    }
    // utilisation d'un builder qui est un design partern d'une usine
    public void envoyerClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alerte_titre);
        builder.setMessage(R.string.alerte_confirmer);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                envoyer();
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        builder.show();
    }

    public void envoyer(){
        EditText etIntitule = findViewById(R.id.intituleText);
        String intitule = etIntitule.getText().toString();

        CheckBox cbUrgent = findViewById(R.id.checkBox);

        if (cbUrgent.isChecked()){
            Toast.makeText(this, getString(R.string.alerte_envoi_urgent, intitule), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.alerte_envoi, intitule), Toast.LENGTH_LONG).show();
        }
        //finish();
    }
}