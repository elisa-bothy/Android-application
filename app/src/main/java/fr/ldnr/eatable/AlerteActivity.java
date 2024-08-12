package fr.ldnr.eatable;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AlerteActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alerte);
        String[] lieux = getResources().getStringArray(R.array.alerte_lieux);
        AutoCompleteTextView etLieu = findViewById(R.id.et_lieu);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, lieux);
        etLieu.setAdapter(aa);
    }

    public void envoyerClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alerte_titre);
        builder.setMessage(R.string.alerte_confirmer);
        builder.setIcon(R.mipmap.oceano_round);
//        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                envoyer(view);
//            }
//        });
        builder.setPositiveButton(android.R.string.yes, (dialogInterface, i)->envoyer());
        builder.setNegativeButton(android.R.string.no, null);
        builder.show();
    }

    public void envoyer(){
        CheckBox cb = findViewById(R.id.cb);
        EditText etIntitule = findViewById(R.id.et_intitule);
        String intitule = etIntitule.getText().toString();
        if (cb.isChecked()){
            String textAlert = getString(R.string.alerte_envoi_urgent, intitule);
            Toast.makeText(this, textAlert, Toast.LENGTH_LONG).show();
        }else{
            String textAlert = getString(R.string.alerte_envoi, intitule);
            Toast.makeText(this, textAlert, Toast.LENGTH_LONG).show();
        }
    }
}
