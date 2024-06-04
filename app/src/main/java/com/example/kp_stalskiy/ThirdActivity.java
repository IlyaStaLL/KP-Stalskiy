package com.example.kp_stalskiy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ThirdActivity extends AppCompatActivity {

    private DatabaseReference userDataRef;
    private PieChart pieChart;
    private TextView userCountTextView;
    private TextView lastRegisteredUserTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        userDataRef = FirebaseDatabase.getInstance().getReference("userData");
        pieChart = findViewById(R.id.pieChart);
        userCountTextView = findViewById(R.id.userCountTextView);
        lastRegisteredUserTextView = findViewById(R.id.lastRegisteredUserTextView);

        loadChartData();
        updateUserCount();

        Button showLastRegisteredUserButton = findViewById(R.id.showLastRegisteredUserButton);
        showLastRegisteredUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLastRegisteredUserInformation();
            }
        });
    }

    private void loadChartData() {
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int maleCount = 0;
                int femaleCount = 0;
                int otherGenderCount = 0;
                int russianCount = 0;
                int otherCountryCount = 0;
                int unspecifiedCount = 0;

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String gender = childSnapshot.child("gender").getValue(String.class);
                    String country = childSnapshot.child("country").getValue(String.class);

                    if (gender != null) {
                        if (gender.equals("Мужской")) {
                            maleCount++;
                        } else if (gender.equals("Женский")) {
                            femaleCount++;
                        } else {
                            otherGenderCount++;
                        }
                    }

                    if (country != null) {
                        if (country.equals("Российское")) {
                            russianCount++;
                        } else if (!country.isEmpty() && !country.equals("не указано")) {
                            otherCountryCount++;
                        } else {
                            unspecifiedCount++;
                        }
                    }
                }

                createPieChart(maleCount, femaleCount, otherGenderCount, russianCount, otherCountryCount, unspecifiedCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ThirdActivity.this, "Произошла ошибка при получении информации", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPieChart(int maleCount, int femaleCount, int otherGenderCount, int russianCount, int otherCountryCount, int unspecifiedCount) {
        ArrayList<PieEntry> genderEntries = new ArrayList<>();
        genderEntries.add(new PieEntry(maleCount, "Мужской"));
        genderEntries.add(new PieEntry(femaleCount, "Женский"));
        genderEntries.add(new PieEntry(otherGenderCount, "Не указано (пол)"));

        PieDataSet genderDataSet = new PieDataSet(genderEntries, "Гендерное соотношение");
        genderDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData genderData = new PieData(genderDataSet);

        pieChart.setData(genderData);
        pieChart.getDescription().setText("Гендерное соотношение");
        pieChart.invalidate(); // refresh

        PieChart nationalityChart = findViewById(R.id.nationalityPieChart);
        ArrayList<PieEntry> nationalityEntries = new ArrayList<>();
        nationalityEntries.add(new PieEntry(russianCount, "Российское"));
        nationalityEntries.add(new PieEntry(otherCountryCount, "Другое"));
        nationalityEntries.add(new PieEntry(unspecifiedCount, "Не указано (гражданство)"));

        PieDataSet nationalityDataSet = new PieDataSet(nationalityEntries, "Гражданство");
        nationalityDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData nationalityData = new PieData(nationalityDataSet);

        nationalityChart.setData(nationalityData);
        nationalityChart.getDescription().setText("Гражданство");
        nationalityChart.invalidate(); // refresh
    }

    private void updateUserCount() {
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long userCount = dataSnapshot.getChildrenCount();
                userCountTextView.setText("Количество граждан принявших участие в переписи: " + userCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ThirdActivity.this, "Произошла ошибка при получении информации", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLastRegisteredUserInformation() {
        userDataRef.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String lastRegisteredUserName = childSnapshot.child("fullName").getValue(String.class);
                    String lastRegisteredUserCountry = childSnapshot.child("country").getValue(String.class);
                    String lastRegisteredUserGender = childSnapshot.child("gender").getValue(String.class);

                    String userInfo = "Имя: " + lastRegisteredUserName + "\n" +
                            "Страна: " + lastRegisteredUserCountry + "\n" +
                            "Пол: " + lastRegisteredUserGender;

                    lastRegisteredUserTextView.setText(userInfo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ThirdActivity.this, "Произошла ошибка при получении информации", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

