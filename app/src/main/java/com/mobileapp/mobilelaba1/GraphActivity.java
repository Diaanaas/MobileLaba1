package com.mobileapp.mobilelaba1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.softmoore.android.graphlib.Graph;
import com.softmoore.android.graphlib.GraphView;
import com.softmoore.android.graphlib.Point;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class GraphActivity extends AppCompatActivity {

    private final String fileName = "credit_calculations.txt"; // Ім'я файлу для збереження даних

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // Зчитування даних з файлу
        String[] fileData = readDataFromFile();
        if (fileData == null || fileData.length < 3) {
            Toast.makeText(this, "Помилка зчитування даних з файлу", Toast.LENGTH_SHORT).show();
            return;
        }

        // Перетворення зчитаних даних
        double initialAmount;
        double monthlyPayment;
        String program;

        try {
            program = fileData[0];
            initialAmount = Double.parseDouble(fileData[1]);
            monthlyPayment = Double.parseDouble(fileData[2]);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Невірний формат даних", Toast.LENGTH_SHORT).show();
            return;
        }

        // Підготовка точок для графіку
        Point[] points = createGraphPoints(initialAmount, monthlyPayment, program);

        // Створення графіку
        Graph graph = new Graph.Builder()
                .setWorldCoordinates(0, 12, 0, initialAmount) // Встановлення координат (місяці, залишок)
                .addLineGraph(points) // Додавання лінійного графіку
                .build();

        // Підключення GraphView до макета
        GraphView graphView = findViewById(R.id.graphView);
        graphView.setGraph(graph);
    }

    // Метод для зчитування даних з файлу
    private String[] readDataFromFile() {
        StringBuilder fileContent = new StringBuilder();
        try {
            FileInputStream fis = openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            Toast.makeText(this, "Помилка під час зчитування файлу: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
        // Розділення даних на окремі частини (сума, платіж, програма)
        return fileContent.toString().split("\n");
    }

    // Метод для підготовки точок для графіку
    private Point[] createGraphPoints(double initialAmount, double monthlyPayment, String program) {
        Point[] points = new Point[12];
        double remainingAmount = initialAmount;

        switch (program) {
            case "Standard":
                for (int i = 0; i < 12; i++) {
                    remainingAmount -= monthlyPayment;
                    points[i] = new Point(i + 1, remainingAmount);
                }
                break;

            case "Premium":
                for (int i = 0; i < 12; i++) {
                    remainingAmount -= (monthlyPayment + (monthlyPayment * 0.05 * i));
                    points[i] = new Point(i + 1, remainingAmount);
                }
                break;

            default:
                for (int i = 0; i < 12; i++) {
                    points[i] = new Point(i + 1, initialAmount);
                }
        }

        return points;
    }
}