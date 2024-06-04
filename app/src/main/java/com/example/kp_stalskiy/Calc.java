package com.example.kp_stalskiy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Calc extends AppCompatActivity {

    private EditText editTextInitialPopulation;
    private EditText editTextGrowthRate;
    private EditText editTextYears;
    private TextView editTextResult;
    private Button buttonCalculate;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        editTextInitialPopulation = findViewById(R.id.editTextInitialPopulation);
        editTextGrowthRate = findViewById(R.id.editTextGrowthRate);
        editTextYears = findViewById(R.id.editTextYears);
        editTextResult = findViewById(R.id.editTextResult);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        database = FirebaseDatabase.getInstance();

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePopulation();
            }
        });

        Button buttonTransferPopulation = findViewById(R.id.buttonTransferPopulation);
        buttonTransferPopulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferPopulationCountFromDatabase();
            }
        });
    }

    private void calculatePopulation() {
        int initialPopulation = Integer.parseInt(editTextInitialPopulation.getText().toString());
        double growthRate = Double.parseDouble(editTextGrowthRate.getText().toString());
        int years = Integer.parseInt(editTextYears.getText().toString());

        // Рассчитываем новое население
        double newPopulation = initialPopulation + (initialPopulation * (growthRate / 100) * years);

        // Выводим результат на экран
        editTextResult.setText(String.valueOf(newPopulation));
    }

    private void transferPopulationCountFromDatabase() {
        database.getReference("phoneNumbers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long userCount = dataSnapshot.getChildrenCount();
                editTextInitialPopulation.setText(String.valueOf(userCount)); // Используем editTextInitialPopulation для отображения количества пользователей
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                editTextInitialPopulation.setText("Ошибка получения данных");
            }
        });
    }
}
