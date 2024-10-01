package com.mobileapp.mobilelaba1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.softmoore.android.graphlib.Graph;
import com.softmoore.android.graphlib.GraphView;
import com.softmoore.android.graphlib.Point;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // Отримання даних з Intent
        double initialAmount = getIntent().getDoubleExtra("initialAmount", 0);
        double monthlyPayment = getIntent().getDoubleExtra("monthlyPayment", 0);
        String program = getIntent().getStringExtra("program");

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

            case "Business":
                for (int i = 0; i < 12; i++) {
                    if (i > 2) remainingAmount -= monthlyPayment;
                    points[i] = new Point(i + 1, remainingAmount);
                }
                break;

            case "Flexible":
                double monthlyInterestRate = 0.02;
                for (int i = 0; i < 12; i++) {
                    double flexiblePayment = remainingAmount * monthlyInterestRate;
                    remainingAmount -= flexiblePayment;
                    points[i] = new Point(i + 1, remainingAmount);
                }
                break;

            case "Annuitet":
                double annualRate = 0.12;
                double annuitetPayment = (initialAmount * (annualRate / 12)) / (1 - Math.pow(1 + (annualRate / 12), -12));
                for (int i = 0; i < 12; i++) {
                    remainingAmount -= annuitetPayment;
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