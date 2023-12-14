package com.example.tamagotchi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView animalListView;
    static ArrayAdapter adapter;
    private ArrayList<Animal> animals;
    private Fragment_add addFragment;
    private PetDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        animalListView = findViewById(R.id.animalListView);// Нахождение ListView в макете
        databaseHelper = new PetDatabaseHelper(this);// Инициализация PetDatabaseHelper
        animals = new ArrayList<>(databaseHelper.getAllAnimals());// Загрузка данных из бд
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, animals);
        animalListView.setAdapter(adapter);

        addFragment = new Fragment_add();

        animals.add(new Animal("Cat", 50, 50, "cat"));
        animals.add(new Animal("Dog", 50, 50, "dog"));

        adapter.notifyDataSetChanged();

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, addFragment);
                transaction.commit();
            }
        });

      // ArrayAdapter<Animal> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, animals);// Создание ArrayAdapter для отображения списка питомцев
      // animalListView.setAdapter(adapter);
       animalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal selectedAnimal = animals.get(position);// Получение выбранного животного

                Intent intent = new Intent(MainActivity.this, Pet.class);// Запуск новой активити для отображения информации о выбранном животном
                intent.putExtra("animalName", selectedAnimal.getName());
                startActivity(intent);
            }
        });
    }
}
 /* animalListView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("animal", animals.get(position)); // Передаем выбранный объект Computer

            int idPets = (int) animals.get(position).getId();
            String nameComp = animals.get(position).getName();
            int happiness = animals.get(position).getHappiness();
            int hunger = animals.get(position).getHunger();
            String Type = animals.get(position).getType();
            bundle.putInt("id", idPets);
            bundle.putString("name", nameComp);
            bundle.putInt("happiness", happiness);
            bundle.putInt("hunger", hunger);
            bundle.putString("type", Type);

        });*/
