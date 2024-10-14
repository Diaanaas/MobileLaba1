package com.mobileapp.mobilelaba1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class CalculationActivity extends AppCompatActivity {

    private TextView resultTextView;
    private Button saveButton, graphButton;
    private final String fileName = "credit_calculations.txt";

    private double initialAmount, monthlyPayment;
    private String program;
    private double remainingAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);

        resultTextView = findViewById(R.id.resultTextView);
        saveButton = findViewById(R.id.saveButton);
        graphButton = findViewById(R.id.graphButton);

        // Отримання даних з Intent
        program = getIntent().getStringExtra("program");
        initialAmount = getIntent().getDoubleExtra("initialAmount", 0);
        monthlyPayment = getIntent().getDoubleExtra("monthlyPayment", 0);

        // Перевірка вхідних даних
        if (initialAmount <= 0 || monthlyPayment <= 0) {
            Toast.makeText(this, "Невірні вхідні дані", Toast.LENGTH_SHORT).show();
            return;
        }

        // Розрахунок залишку заборгованості
        remainingAmount = calculateRemainingAmount(initialAmount, monthlyPayment, program);

        // Виведення результату
        String resultText = "Програма: " + program + "\n" +
                "Початкова сума: " + initialAmount + "\n" +
                "Періодичний платіж: " + monthlyPayment + "\n" +
                "Залишок заборгованості: " + remainingAmount;

        resultTextView.setText(resultText);

        // Збереження даних у файл
        saveButton.setOnClickListener(v -> saveDataToFile(program, initialAmount, monthlyPayment));

        // Перехід до відображення графіку
        graphButton.setOnClickListener(v -> {
            Intent intent = new Intent(CalculationActivity.this, GraphActivity.class);
            intent.putExtra("initialAmount", initialAmount);
            intent.putExtra("monthlyPayment", monthlyPayment);
            intent.putExtra("program", program);
            startActivity(intent);
        });
    }

    private double calculateRemainingAmount(double initialAmount, double monthlyPayment, String program) {
        double remainingAmount = initialAmount;

        // Перевірка на випадок невідомої програми
        if (program == null || program.isEmpty()) {
            Toast.makeText(this, "Невідома програма кредитування", Toast.LENGTH_SHORT).show();
            return remainingAmount;
        }

        // Місячна процентна ставка
        double monthlyInterestRate = 0.02;

        switch (program) {
            case "Standard":
                remainingAmount = initialAmount - (monthlyPayment * 12);
                break;
            case "Premium":
                for (int i = 0; i < 12; i++) {
                    remainingAmount -= (monthlyPayment + (monthlyPayment * 0.05 * i));
                }
                break;
            default:
                Toast.makeText(this, "Невідома програма: " + program, Toast.LENGTH_SHORT).show();
        }

        return remainingAmount;
    }

    private void saveDataToFile(String program, double initialAmount, double monthlyPayment) {
        String data = program + "\n" + initialAmount + "\n" + monthlyPayment;

        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
            Toast.makeText(this, "Дані збережено у файл: " + fileName, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Помилка збереження даних у файл", Toast.LENGTH_SHORT).show();
        }
    }
}
