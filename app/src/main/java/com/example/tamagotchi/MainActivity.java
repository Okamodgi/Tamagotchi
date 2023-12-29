package com.example.tamagotchi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

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

        animalListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, addFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

       animalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal selectedAnimal = animals.get(position);// Получение выбранного животного

                Intent intent = new Intent(MainActivity.this, Pet.class);// Запуск новой активити
                intent.putExtra("animalName", selectedAnimal.getName());
                startActivity(intent);
            }
        });
    }
    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удаление")
                .setMessage("Вы уверены, что хотите сдать в приют этого питомца?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAnimal(position);
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
    private void deleteAnimal(int position) {
        Animal deletedAnimal = animals.get(position);
        databaseHelper.deleteAnimal(deletedAnimal);
        animals.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Питомец удален", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("updatedAnimal")) {
                Animal updatedAnimal = (Animal) data.getSerializableExtra("updatedAnimal");

                animals.add(updatedAnimal);
                adapter.notifyDataSetChanged();

            }
        }
    }
    public void updateAnimalList() {
        animals.clear();
        animals.addAll(databaseHelper.getAllAnimals());
        adapter.notifyDataSetChanged();
    }
}

//синглтон