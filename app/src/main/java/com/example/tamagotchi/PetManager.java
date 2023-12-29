/*package com.example.tamagotchi;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PetManager {
    private static PetManager instance;
    private PetDatabaseHelper dbHelper;
    private Animal currentAnimal;

    private PetManager(Context context) {
        dbHelper = new PetDatabaseHelper(context);
    }

    public static synchronized PetManager getInstance(Context context) {
        if (instance == null) {
            instance = new PetManager(context.getApplicationContext());
        }
        return instance;
    }

    public Animal getCurrentAnimal() {
        return currentAnimal;
    }

    public void setCurrentAnimal(Animal animal) {
        this.currentAnimal = animal;
    }

    public void saveCurrentAnimal() {
        if (currentAnimal != null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                ContentValues values = new ContentValues();
                values.put("happiness", currentAnimal.getHappiness());
                values.put("hunger", currentAnimal.getHunger());

                int rowsAffected = db.update("animal", values, "_id=?", new String[]{String.valueOf(currentAnimal.getId())});
                Log.d("PetManager", "Rows affected: " + rowsAffected);
            } catch (Exception e) {
                Log.e("PetManager", "Error saving to database: " + e.getMessage());
            } finally {
                db.close();
            }
        }
    }

    public void loadCurrentAnimal(String animalName) {
        currentAnimal = loadAnimalFromDatabase(animalName);
    }

    @SuppressLint("Range")
    private Animal loadAnimalFromDatabase(String animalName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Animal loadedAnimal = null;

        try {
            String[] columns = {"_id", "name", "happiness", "hunger", "type"};
            String selection = "name=?";
            String[] selectionArgs = {animalName};

            Cursor cursor = db.query("animal", columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                loadedAnimal = new Animal(
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getInt(cursor.getColumnIndex("happiness")),
                        cursor.getInt(cursor.getColumnIndex("hunger")),
                        cursor.getString(cursor.getColumnIndex("type"))
                );

                loadedAnimal.setId(cursor.getLong(cursor.getColumnIndex("_id")));
            }

            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return loadedAnimal;
    }
}

 */

