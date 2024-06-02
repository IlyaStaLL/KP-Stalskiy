package com.example.kp_stalskiy;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static final String KEY = "key";
    private DatabaseReference myData;
    private EditText phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myData = FirebaseDatabase.getInstance().getReference("userData");

        phoneEditText = findViewById(R.id.editTextPhone);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        if (v.getId() == R.id.button) {
            String phoneNumber = phoneEditText.getText().toString().trim();

            if (phoneNumber.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Пожалуйста, укажите свой номер телефона", Toast.LENGTH_SHORT).show();
            } else if (phoneNumber.equals("1234")) {
                // диалоговое окно для ввода пароля
                showPasswordDialog();
            } else {
                startSecondActivity(phoneNumber);
            }
        }
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
                    String phoneNumber = phoneEditText.getText().toString().trim();  // Получаем номер телефона из EditText
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