package com.example.tamagotchi;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class Pet extends AppCompatActivity {

    public static ArrayAdapter<Object> adapter;
    private PetDatabaseHelper dbHelper;
    private Animal animal;
    private int happiness = 50;
    private int hunger = 50;
    private String type ;
    private TextView happinessTextView;
    private TextView hungerTextView;
    private ImageView tamagotchiImageView;
    private ImageView imagePet;

    private Handler handler = new Handler();

    // Переменные для взаимодействия с базой данных и хранения настроек
    private SharedPreferences sharedPref;

    private static final String KEY_HAPPINESS = "happiness";
    private static final String KEY_HUNGER = "hunger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet);

        dbHelper = new PetDatabaseHelper(this);

        tamagotchiImageView = findViewById(R.id.tamagotchiImageView);
        imagePet = findViewById(R.id.imagePet);
        happinessTextView = findViewById(R.id.happinessTextView);
        hungerTextView = findViewById(R.id.hungerTextView);

        Intent intent = getIntent();
        String animalName = intent.getStringExtra("animalName");

        setTitle(animalName);

        animal = loadAnimalFromDatabase(animalName);

        tamagotchiImageView.setImageResource(animal.getImageResourceId());


        Button feedButton = findViewById(R.id.feedButton);
        Button playButton = findViewById(R.id.playButton);
        Button backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnimalToDatabase();
                saveInstanceStateToSharedPreferences(animal.getName());
                MainActivity.adapter.notifyDataSetChanged();
                onBackPressed();
            }
        });

        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedTamagotchi();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomGame = new Random().nextInt(2);

                if (randomGame == 0) {
                    playWithTamagotchi();
                } else {
                    playWithTamagotchi2();
                }
            }
        });

        handler.postDelayed(tamagotchiRunnable, 10000);
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
                loadedAnimal.setType(cursor.getString(cursor.getColumnIndex("type")));

                type = loadedAnimal.getType();

                if ("Кот".equals(type)) {
                    imagePet.setImageResource(R.drawable.kot);
                } else if ("Попугай".equals(type)) {
                    imagePet.setImageResource(R.drawable.popuga);
                } else {
                    imagePet.setImageResource(R.drawable.sobak);
                }

                tamagotchiImageView.setImageResource(loadedAnimal.getImageResourceId());
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

    private void feedTamagotchi() {
        hunger = Math.max(0, hunger - 10);
        if (hunger > 0) {
            happiness = Math.min(100, happiness + 5);
        }
        updateUI();
        saveAnimalToDatabase();
    }
    private void increaseHappiness(int amount) {
        happiness = Math.min(100, happiness + amount);
        updateUI();
        saveAnimalToDatabase();
    }

    private void playWithTamagotchi() {
        Intent intent = new Intent(Pet.this, miniGameQuiz.class);
        startActivity(intent);
        increaseHappiness(10);
    }
    private void playWithTamagotchi2() {
        Intent intent = new Intent(Pet.this, miniGameSearch.class);
        startActivity(intent);
        increaseHappiness(10);
    }

    private void updateUI() {
        happinessTextView.setText("счастье: " + happiness);
        hungerTextView.setText("голод: " + hunger);
        if (happiness > 70 && hunger < 30) {
            tamagotchiImageView.setImageResource(R.drawable.over_happy);
        } else if (happiness > 30 && hunger < 70) {
            tamagotchiImageView.setImageResource(R.drawable.happy);
        } else {
            tamagotchiImageView.setImageResource(R.drawable.don_t_happy);
        }

    }

    private void saveAnimalToDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("happiness", happiness);
            values.put("hunger", hunger);

            int rowsAffected = db.update("animal", values, "_id=?", new String[]{String.valueOf(animal.getId())});
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }

    private Runnable tamagotchiRunnable = new Runnable() {
        @Override
        public void run() {
            happiness = Math.max(0, happiness - 5);
            hunger = Math.min(100, hunger + 5);

            updateUI();
            saveAnimalToDatabase();

            handler.postDelayed(this, 25000);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        saveAnimalToDatabase();
        saveInstanceStateToSharedPreferences(animal.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        animal = loadAnimalFromDatabase(animal.getName());
        restoreInstanceStateFromSharedPreferences(animal.getName());
        updateUI();
    }

    private void saveInstanceStateToSharedPreferences(String animalName) {
        sharedPref = getSharedPreferences(animalName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_HAPPINESS, happiness);
        editor.putInt(KEY_HUNGER, hunger);
        editor.apply();
    }

    private void restoreInstanceStateFromSharedPreferences(String animalName) {
        sharedPref = getSharedPreferences(animalName, Context.MODE_PRIVATE);
        happiness = sharedPref.getInt(KEY_HAPPINESS, 50);
        hunger = sharedPref.getInt(KEY_HUNGER, 50);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_HAPPINESS, happiness);
        outState.putInt(KEY_HUNGER, hunger);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        happiness = savedInstanceState.getInt(KEY_HAPPINESS, 50);
        hunger = savedInstanceState.getInt(KEY_HUNGER, 50);
        updateUI();
    }
}
