package com.example.tamagotchi;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Fragment_add extends Fragment {
    private EditText name;
    private EditText type;
    private Drawable originalBackground;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        PetDatabaseHelper petDatabaseHelper = new PetDatabaseHelper(requireContext());
        Drawable back = getActivity().getWindow().getDecorView().getBackground();
        name = view.findViewById(R.id.name);
        type = view.findViewById(R.id.type_of_animal);
        view.setBackground(back);

        setEditTextFocus(name);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditTextFocus(name);
            }
        });

        Button catButton = view.findViewById(R.id.cat);
        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type.setText("Кот");
            }
        });

        Button parrotButton = view.findViewById(R.id.parrot);
        parrotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type.setText("Попугай");
            }
        });

        Button dogButton = view.findViewById(R.id.dog);
        dogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type.setText("Собака");
            }
        });
        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedName = name.getText().toString();
                String editedType = type.getText().toString();

                if (editedName.isEmpty() || editedType.isEmpty()) {
                    Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                } else {
                    Animal newAnimal = new Animal(editedName, 50, 50, editedType);

                    petDatabaseHelper.addPets(editedName, "50", "50", editedType);

                    Log.d("Fragment_add", "Data saved to database: " + editedName + ", " + editedType);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedAnimal", newAnimal);

                    requireActivity().setResult(requireActivity().RESULT_OK, resultIntent);
                    requireActivity().getSupportFragmentManager().popBackStack();
                    ((MainActivity) requireActivity()).updateAnimalList();

                    requireActivity().finish();}
            }
    });

        Button backButton1 = view.findViewById(R.id.back1);
        backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
                getActivity().getWindow().getDecorView().setBackground(originalBackground);
                ((MainActivity) requireActivity()).updateAnimalList();
            }
        });
        return view;
    }

    private void setEditTextFocus(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.setCursorVisible(true);
        editText.requestFocus();
    }
}
