package com.example.tamagotchi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class miniGameSearch extends AppCompatActivity {
    private int score = 0;
    private boolean toastDisplayed = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mini_game_search);
        final RelativeLayout mainLayout = findViewById(R.id.mainLayout);
        final ImageButton button = new ImageButton(this);

        button.setBackgroundResource(R.drawable.bones);

        // Устанавливаем начальное положение кнопки
        setRandomPosition(button, mainLayout);

        // Добавляем кнопку в макет
        mainLayout.addView(button);
        if (!toastDisplayed) {
            showToast("Найдите кость");
            toastDisplayed = true;
        }

        // Устанавливаем слушатель событий для кнопки
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // При нажатии перемещаем кнопку в случайное место
                setRandomPosition(button, mainLayout);

                // Увеличиваем счет
                score++;

                // Проверяем, достигнуто ли 4 очка
                if (score == 4) {
                    // Закрываем активность
                    finish();
                }
            }
        });
    }

    private void setRandomPosition(View view, ViewGroup parent) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                dpToPx(60),
                dpToPx(60));

        // Генерируем случайные координаты в пределах родительского представления
        int maxX = parent.getWidth() - view.getWidth();
        int maxY = parent.getHeight() - view.getHeight();

        Random random = new Random();
        int randomX = random.nextInt(maxX + 1);
        int randomY = random.nextInt(maxY + 1);

        // Устанавливаем новые координаты
        layoutParams.leftMargin = randomX;
        layoutParams.topMargin = randomY;

        // Применяем новые параметры макета
        view.setLayoutParams(layoutParams);
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
