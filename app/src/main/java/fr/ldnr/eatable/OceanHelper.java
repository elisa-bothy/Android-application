package fr.ldnr.eatable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OceanHelper extends SQLiteOpenHelper {
    public OceanHelper(@Nullable Context context) {
        super(context, "ocean.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE especes(id INTEGER PRIMARY KEY, nom TEXT)");
        String insertionEspece = "INSERT INTO especes(nom) VALUES (?)";
        db.execSQL(insertionEspece, new Object[]{"Baleine"});
        db.execSQL(insertionEspece, new Object[]{"Dauphin"});
        db.execSQL(insertionEspece, new Object[]{"Pinguin"});
        db.execSQL(insertionEspece, new Object[]{"Orque"});
        db.execSQL(insertionEspece, new Object[]{"Requin"});
        db.execSQL(insertionEspece, new Object[]{"Raie"});
        db.execSQL(insertionEspece, new Object[]{"Ours blanc"});
        db.execSQL(insertionEspece, new Object[]{"Okari"});
        db.execSQL(insertionEspece, new Object[]{"Phoque"});
        db.execSQL("CREATE TABLE animaux(id INTEGER PRIMARY KEY, nom TEXT, age INTEGER, " +
                "id_especes INTEGER, FOREIGN KEY (id_especes) REFERENCES especes(id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int insererAnimal(String nom, int age, String espece){
        SQLiteDatabase db = getWritableDatabase();
        int id_especes = getIdEspece(espece); //TODO vraiment trouver l'especes
        db.execSQL("INSERT INTO animaux(nom, age, id_especes) VALUES (?,?,?)",
                new Object[]{nom, age, id_especes});
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM animaux WHERE id_especes = ?",
                 new String[]{String.valueOf(id_especes)});
        c.moveToNext();
        int resultat = c.getInt(0);
        c.close();
        db.close();
        return resultat;
    }

    private int getIdEspece(String espece) {
        SQLiteDatabase db = getReadableDatabase();
        // LIKE permet que la valeur ne soit pas totalement pareil ; d'éviter la casse
        Cursor c = db.rawQuery("SELECT id FROM especes WHERE nom LIKE ?",
                new String[]{String.valueOf(espece)});
        c.moveToNext();
        int id = c.getInt(0);
        c.close();
        return id;
    }

    public List<String> getAnimaux(){
        List<String> animaux = new ArrayList<>();

        String sql = "SELECT * FROM animaux " +
                "JOIN especes ON id_especes=especes.id ORDER BY age";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        while(c.moveToNext()){
            animaux.add(c.getString(1)+"|"+c.getInt(2)+"|"+
                    c.getString(5));
        }
        c.close();
        db.close();
        return animaux;
    }
}
