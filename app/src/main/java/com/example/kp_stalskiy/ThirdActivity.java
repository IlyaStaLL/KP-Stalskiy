package com.example.kp_stalskiy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {

    public static final String KEY = "key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        TextView textView = findViewById(R.id.textView4);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String phoneNumber = bundle.getString(KEY);
            textView.setText("Вы указали номер телефона admin: " + phoneNumber);
        }
    }
}
