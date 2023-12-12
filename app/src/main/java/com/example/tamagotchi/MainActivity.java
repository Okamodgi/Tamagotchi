package com.example.tamagotchi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView animalListView;
    private PetDatabaseHelper databaseHelper;
    private List<Animal> animals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        // Инициализируем PetDatabaseHelper
        databaseHelper = new PetDatabaseHelper(this);

        // Загружаем данные из базы данных
        animals = databaseHelper.getAllAnimals();

        // Находим ListView в макете
        animalListView = findViewById(R.id.animalListView);

        // Создаем ArrayAdapter для отображения списка животных
        ArrayAdapter<Animal> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, animals);

        // Устанавливаем адаптер для ListView
        animalListView.setAdapter(adapter);

        // Устанавливаем слушателя событий для обработки нажатия на элемент списка
        animalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранное животное
                Animal selectedAnimal = animals.get(position);

                // Запускаем вторую активити для отображения информации о выбранном животном
                Intent intent = new Intent(MainActivity.this, Pet.class);
                intent.putExtra("animalName", selectedAnimal.getName());
                intent.putExtra("animalDescription", selectedAnimal.getDescription());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Закрываем базу данных при уничтожении активности
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}