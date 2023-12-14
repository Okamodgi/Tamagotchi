package com.example.tamagotchi;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Fragment_add extends Fragment {
    private EditText name;
    private EditText type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        PetDatabaseHelper petDatabaseHelper = new PetDatabaseHelper(requireContext());

        EditText name = view.findViewById(R.id.name);
        EditText type = view.findViewById(R.id.type_of_animal);

        setEditTextFocus(name);
        setEditTextFocus(type);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditTextFocus(name);
            }
        });
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditTextFocus(type);
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

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedAnimal", newAnimal);

                    requireActivity().setResult(requireActivity().RESULT_OK, resultIntent);

                    requireActivity().finish();
                }
            }
        });

        Button backButton1 = view.findViewById(R.id.back1);
        backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
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

                    /*PetDatabaseHelper dbHelper = new PetDatabaseHelper(requireContext());
                    dbHelper.addPets(editedName, "50", "50", editedType);*/