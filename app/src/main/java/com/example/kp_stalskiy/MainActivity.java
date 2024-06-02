package com.example.kp_stalskiy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static final String KEY = "key";
    private DatabaseReference myData;
    private String USER_PHONE = "Phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(this::onClick);
        myData = FirebaseDatabase.getInstance().getReference(USER_PHONE);
    }

    private void onClick(View v) {
        if (v.getId() == R.id.button) {
            EditText phonetext = findViewById(R.id.editTextPhone);
            String phoneNumber = phonetext.getText().toString().trim();

            if (phoneNumber.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Пожалуйста, укажите свой номер телефона", Toast.LENGTH_SHORT).show();
            } else if (phoneNumber.equals("88005553535")) {
                // диалоговое окно для ввода пароля
                showPasswordDialog();
            } else {
                checkPhoneNumberInDatabase(phoneNumber);
            }
        }
    }

    private void checkPhoneNumberInDatabase(String phoneNumber) {
        myData.child(phoneNumber).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Номер телефона уже существует в базе
                            String existingNumber = dataSnapshot.getValue(String.class);
                            Toast.makeText(getApplicationContext(), "Данный номер телефона уже зарегистрирован", Toast.LENGTH_SHORT).show();
                        } else {
                            // Номер телефона не существует в базе
                            savePhoneNumberToDatabase(phoneNumber);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Ошибка при обращении к базе данных", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void savePhoneNumberToDatabase(String phoneNumber) {
        myData.child(phoneNumber).setValue(phoneNumber)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Новый номер телефона успешно зарегистрирован", Toast.LENGTH_SHORT).show();
                        startSecondActivity(phoneNumber);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при регистрации номера телефона", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startSecondActivity(String phoneNumber) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra(KEY, phoneNumber);
        startActivity(intent);
    }

    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите пароль от аккаунта admin");

        // Добавляем поле для ввода пароля в диалоговое окно
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = input.getText().toString();

                // Проверяем пароль
                if (password.equals("1234")) {
                    EditText phonetext = findViewById(R.id.editTextPhone);
                    String phoneNumber = phonetext.getText().toString().trim();  // Получаем номер телефона из EditText
                    Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                    intent.putExtra(KEY, phoneNumber);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Неверный пароль", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}