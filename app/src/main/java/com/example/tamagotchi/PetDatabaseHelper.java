package com.example.tamagotchi;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PetDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "animal_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ANIMAL = "animal";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_HAPPINESS = "happiness";
    private static final String COLUMN_HUNGER = "hunger";


    public PetDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ANIMAL + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_HAPPINESS + " INTEGER,"
                + COLUMN_HUNGER + " INTEGER);");

        ContentValues values1 = new ContentValues();
        values1.put(COLUMN_NAME, "PET1");
        values1.put(COLUMN_HAPPINESS, 50);
        values1.put(COLUMN_HUNGER, 50);
        db.insert(TABLE_ANIMAL, null, values1);

        ContentValues values2 = new ContentValues();
        values2.put(COLUMN_NAME, "PET2");
        values2.put(COLUMN_HAPPINESS, 50);
        values2.put(COLUMN_HUNGER, 50);
        db.insert(TABLE_ANIMAL, null, values2);
    }
    public void addPets(String name, String happiness, String hunger) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_HAPPINESS, happiness);
        values.put(COLUMN_HUNGER, hunger);
        db.insert(TABLE_ANIMAL, null, values);
        db.close();}


   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       try {
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMAL);
       } catch (Exception exception) {
           exception.printStackTrace();
       } finally {
           onCreate(db);
       }
   }
    @SuppressLint("Range")
   public List<Animal> getAllAnimals() {
        List<Animal> animals = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ANIMAL, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                int happiness = cursor.getInt(cursor.getColumnIndex(COLUMN_HAPPINESS));
                int hunger = cursor.getInt(cursor.getColumnIndex(COLUMN_HUNGER));
                animals.add(new Animal(name, happiness, hunger));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return animals;
    }
}



