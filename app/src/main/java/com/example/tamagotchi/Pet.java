package com.example.tamagotchi;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Pet extends AppCompatActivity {
    private PetDatabaseHelper dbHelper;
    private Animal animal;

    private int happiness = 50;
    private int hunger = 50;
    private String type = "cat";

    private TextView happinessTextView;
    private TextView hungerTextView;
    private ImageView tamagotchiImageView;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet);

        dbHelper = new PetDatabaseHelper(this);

        // Получение информации о животном из интента или базы данных
        Intent intent = getIntent();
        String animalName = intent.getStringExtra("animalName");
        String animalDescription = intent.getStringExtra("animalDescription");

        // Использование инфы
        setTitle(animalName);

        animal = loadAnimalFromDatabase(animalName);

        happinessTextView = findViewById(R.id.happinessTextView);
        hungerTextView = findViewById(R.id.hungerTextView);
        tamagotchiImageView = findViewById(R.id.tamagotchiImageView);

        Button feedButton = findViewById(R.id.feedButton);
        Button playButton = findViewById(R.id.playButton);
        Button backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                playWithTamagotchi();
            }
        });

        // обновление состояния Тамагочи
        handler.postDelayed(tamagotchiRunnable, 10000); // каждые 10 секунд
    }

    @SuppressLint("Range")
    private Animal loadAnimalFromDatabase(String animalName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"_id", "name", "happiness", "hunger"};
        String selection = "name=?";
        String[] selectionArgs = {animalName};

        Cursor cursor = db.query("animal", columns, selection, selectionArgs, null, null, null);

        Animal loadedAnimal = new Animal(animalName, happiness, hunger, type);

        if (cursor.moveToFirst()) {
            loadedAnimal.setId(cursor.getLong(cursor.getColumnIndex("_id")));
            loadedAnimal.setName(cursor.getString(cursor.getColumnIndex("name")));
            loadedAnimal.setHappiness(cursor.getInt(cursor.getColumnIndex("happiness")));
            loadedAnimal.setHunger(cursor.getInt(cursor.getColumnIndex("hunger")));
            loadedAnimal.setType(cursor.getString(cursor.getColumnIndex("type")));

        }

        cursor.close();
        db.close();

        return loadedAnimal;
    }

    private void feedTamagotchi() {
        hunger = Math.max(0, hunger - 10);
        updateUI();

        saveAnimalToDatabase();
    }

    private void playWithTamagotchi() {
        happiness = Math.min(100, happiness+ 10);
        updateUI();

        saveAnimalToDatabase();
    }

    private void updateUI() {
        happinessTextView.setText("Happiness: " + happiness);
        hungerTextView.setText("Hunger: " + hunger);
        if (happiness > 70 && hunger < 30) {
            tamagotchiImageView.setImageResource(R.drawable.happy_full);
        } else if (happiness > 30 && hunger < 70) {
            tamagotchiImageView.setImageResource(R.drawable.normal);
        } else {
            tamagotchiImageView.setImageResource(R.drawable.sad_hungry);
        }
    }

    private void saveAnimalToDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", animal.getName());
        values.put("happiness", happiness);
        values.put("hunger", hunger);

        db.update("animal", values, "_id=?", new String[]{String.valueOf(animal.getId())});
        db.close();
    }

    private Runnable tamagotchiRunnable = new Runnable() {
        @Override
        public void run() {
            happiness = Math.max(0, happiness - 5);// минус счастье
            hunger = Math.min(100, hunger + 5);// плюс поел

            updateUI();

            saveAnimalToDatabase();// Сохранение

            handler.postDelayed(this, 10000);// следующий запуск через 10 секунд
        }
    };
}

