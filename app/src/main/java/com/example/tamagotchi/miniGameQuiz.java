package com.example.tamagotchi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class miniGameQuiz extends AppCompatActivity {
    private TextView questionTextView;
    private RadioGroup answerRadioGroup;
    private Button submitButton;
    private Button backButton3;

    private List<String> allQuestions = new ArrayList<>();
    private List<String> allCorrectAnswers = new ArrayList<>();
    private List<String> allIncorrectAnswers = new ArrayList<>();
    private List<Integer> chosenQuestionIndexes = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.min_game_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        answerRadioGroup = findViewById(R.id.answerRadioGroup);
        submitButton = findViewById(R.id.submitButton);
        backButton3 = findViewById(R.id.back3);

        initializeQuestions();

        // Выбираем 3 случайных вопроса из общего списка
        chooseRandomQuestions();

        displayQuestion();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        backButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pet.adapter.notifyDataSetChanged();

                onBackPressed();
            }
        });
    }

    private void initializeQuestions() {
        allQuestions.add("Какая столица у Франции?");
        allCorrectAnswers.add("Париж");
        allIncorrectAnswers.add("Лондон");
        allIncorrectAnswers.add("Берлин");

        allQuestions.add("Какая планета известна как Красная Планета?");
        allCorrectAnswers.add("Марс");
        allIncorrectAnswers.add("Венера");
        allIncorrectAnswers.add("Юпитер");

        allQuestions.add("Кто написал 'Ромео и Джульетта'?");
        allCorrectAnswers.add("Уильям Шекспир");
        allIncorrectAnswers.add("Фёдор Достоевский");
        allIncorrectAnswers.add("Джейн Остин");

        allQuestions.add("В каком году была основана Microsoft?");
        allCorrectAnswers.add("1975");
        allIncorrectAnswers.add("1998");
        allIncorrectAnswers.add("1980");

        allQuestions.add("Какой язык программирования был создан Джеймсом Гослингом?");
        allCorrectAnswers.add("Java");
        allIncorrectAnswers.add("C#");
        allIncorrectAnswers.add("Python");

        allQuestions.add("Какое наименование у самого крупного океана на Земле?");
        allCorrectAnswers.add("Тихий");
        allIncorrectAnswers.add("Большой");
        allIncorrectAnswers.add("Атлантический");

        allQuestions.add("Какая страна является родиной хоккея с мячом?");
        allCorrectAnswers.add("Индия");
        allIncorrectAnswers.add("Канада");
        allIncorrectAnswers.add("Финляндия");

        allQuestions.add("Какое животное является символом Австралии?");
        allCorrectAnswers.add("Кенгуру");
        allIncorrectAnswers.add("Паук");
        allIncorrectAnswers.add("Капибара");

        allQuestions.add("Какой город считается столицей кино?");
        allCorrectAnswers.add("Голливуд");
        allIncorrectAnswers.add("Канны");
        allIncorrectAnswers.add("Лос-Анджелес");

        allQuestions.add("Сколько континентов на Земле?");
        allCorrectAnswers.add("6");
        allIncorrectAnswers.add("7");
        allIncorrectAnswers.add("8");
    }

    private void chooseRandomQuestions() {

        List<Integer> allIndexes = new ArrayList<>();
        for (int i = 0; i < allQuestions.size(); i++) {
            allIndexes.add(i);
        }
        // Перемешиваем индексы вопросов и выбираем первые 3
        Collections.shuffle(allIndexes);
        chosenQuestionIndexes.add(allIndexes.get(0));
        chosenQuestionIndexes.add(allIndexes.get(1));
        chosenQuestionIndexes.add(allIndexes.get(2));
    }

    private void displayQuestion() {
        if (currentQuestionIndex < chosenQuestionIndexes.size()) {
            int currentQuestionIndexInList = chosenQuestionIndexes.get(currentQuestionIndex);

            questionTextView.setText(allQuestions.get(currentQuestionIndexInList));
            answerRadioGroup.clearCheck();

            // Создаем список для вариантов ответа и перемешиваем его
            List<String> answerOptions = new ArrayList<>();
            answerOptions.add(allCorrectAnswers.get(currentQuestionIndexInList));
            answerOptions.add(allIncorrectAnswers.get(currentQuestionIndexInList * 2));
            answerOptions.add(allIncorrectAnswers.get(currentQuestionIndexInList * 2 + 1));

            Collections.shuffle(answerOptions);

            // Устанавливаем текст вариантов ответа
            ((RadioButton) findViewById(R.id.option1)).setText("A. " + answerOptions.get(0));
            ((RadioButton) findViewById(R.id.option2)).setText("B. " + answerOptions.get(1));
            ((RadioButton) findViewById(R.id.option3)).setText("C. " + answerOptions.get(2));
        } else {
            showResults();
        }
    }

    private void checkAnswer() {
        int selectedId = answerRadioGroup.getCheckedRadioButtonId();

        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            String selectedAnswer = selectedRadioButton.getText().toString().substring(3);

            int currentQuestionIndexInList = chosenQuestionIndexes.get(currentQuestionIndex);

            if (selectedAnswer.equals(allCorrectAnswers.get(currentQuestionIndexInList))) {
                correctAnswersCount++;
            } else {}
            currentQuestionIndex++;
            displayQuestion();
        } else {
            Toast.makeText(this, "Выберите вариант ответа", Toast.LENGTH_SHORT).show();
        }
    }

    private void showResults() {
        String resultMessage = "Вы ответили правильно на " + correctAnswersCount + " из " + chosenQuestionIndexes.size() + " вопросов.";
        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show();

        submitButton.setVisibility(View.GONE);
        backButton3.setVisibility(View.VISIBLE);
    }
}
