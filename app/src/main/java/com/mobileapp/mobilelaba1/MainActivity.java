package com.mobileapp.mobilelaba1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner creditProgramSpinner;
    private EditText initialAmountEditText, monthlyPaymentEditText;
    private Button calculateButton;

    private String selectedProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        creditProgramSpinner = findViewById(R.id.creditProgramSpinner);
        initialAmountEditText = findViewById(R.id.initialAmountEditText);
        monthlyPaymentEditText = findViewById(R.id.monthlyPaymentEditText);
        calculateButton = findViewById(R.id.calculateButton);

        // Заповнення Spinner з варіантами програм
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.credit_programs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        creditProgramSpinner.setAdapter(adapter);

        creditProgramSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProgram = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedProgram = "Standard";
            }
        });

        calculateButton.setOnClickListener(v -> {
            String initialAmount = initialAmountEditText.getText().toString();
            String monthlyPayment = monthlyPaymentEditText.getText().toString();

            if (initialAmount.isEmpty() || monthlyPayment.isEmpty()) {
                Toast.makeText(MainActivity.this, "Будь ласка, введіть всі дані", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, CalculationActivity.class);
            intent.putExtra("program", selectedProgram);
            intent.putExtra("initialAmount", Double.parseDouble(initialAmount));
            intent.putExtra("monthlyPayment", Double.parseDouble(monthlyPayment));
            startActivity(intent);
        });
    }
    public void openAuthorActivity(View view) {
        Intent intent = new Intent(this, AuthorActivity.class);
        startActivity(intent);
    }
}