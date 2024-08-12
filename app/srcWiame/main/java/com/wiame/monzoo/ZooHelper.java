package com.wiame.monzoo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ZooHelper extends SQLiteOpenHelper {

    public ZooHelper(@Nullable Context context) {
        // fichier zoo.sqlite qui est une norme pour venir gerer la BDD (equivaut a un phpmyadmin)
        super(context, "zoo.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE espece(id INTEGER PRIMARY KEY, nom TEXTE)");
        String insertionEspece = "INSERT INTO espece(nom) VALUES (?)";
        db.execSQL(insertionEspece, new Object[]{"singe"});
        db.execSQL(insertionEspece, new Object[]{"lion"});
        db.execSQL(insertionEspece, new Object[]{"crocodile"});
        db.execSQL(insertionEspece, new Object[]{"giraffe"});
        db.execSQL("CREATE TABLE animal(id INTEGER PRIMARY KEY, nom TEXTE, age NUMBER, id_espece INTEGER, " +
                                        "FOREIGN KEY (id_espece) REFERENCES espece(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int insererAnimal(String nom, int age, String espece){
        int id_espece = getIdEspece(espece);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO animal(nom, age, id_espece) VALUES (?,?,?)", new Object[]{nom, age, id_espece});
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM animal WHERE id_espece=?",
                new String[]{String.valueOf(id_espece)});
        c.moveToNext();
        // recuperation de données
        // cherche la premiere information et la mettre dans une varable
        int resultat = c.getInt(0);
        c.close();
        db.close();
        return resultat;
    }

    private int getIdEspece(String espece) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM espece WHERE nom LIKE ?", new String[]{String.valueOf(espece)});
        c.moveToNext();
        int id = c.getInt(0);
        c.close();
        db.close();
        return id;
    }

    public List<String> getAnimal(){
        List<String> animaux = new ArrayList<>();
        String sql = "SELECT * FROM animal JOIN espece " +
                "ON id_espece = espece.id ORDER BY age";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        while(c.moveToNext()){
            animaux.add(c.getString(1)+ "|" +c.getInt(2)+ "|" + c.getString(5));
        }
        c.close();
        db.close();
        return animaux;
    }
}
