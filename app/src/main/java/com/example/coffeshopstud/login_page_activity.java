package com.example.coffeshopstud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login_page_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_activity);

        // Инициализация элементов интерфейса
        EditText editTextName = findViewById(R.id.log_editTextText);
        EditText editTextPassword = findViewById(R.id.log_editTextText1);
        Button startButton = findViewById(R.id.log_button);
        TextView registerTextView = findViewById(R.id.log_textView9);

        // Обработчик нажатия на кнопку "НАЧАТЬ"
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if (!name.isEmpty() && !password.isEmpty()) {
                    Intent intent = new Intent(login_page_activity.this, main_page_activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(login_page_activity.this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Обработчик нажатия на текст "Зарегистрироваться"
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_page_activity.this, register_page_activity.class);
                startActivity(intent);
            }
        });
    }
}