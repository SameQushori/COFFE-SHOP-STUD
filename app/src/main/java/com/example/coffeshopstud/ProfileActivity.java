package com.example.coffeshopstud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView textViewName;
    private TextView textViewEmail;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Инициализация Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Инициализация UI элементов
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Получение текущего пользователя
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Установка данных пользователя
            String name = user.getDisplayName(); // Если имя доступно
            String email = user.getEmail();

            if (name == null || name.isEmpty()) {
                name = "Имя не указано"; // Заглушка, если имя отсутствует
            }
            if (email == null || email.isEmpty()) {
                email = "Почта не указана"; // Заглушка, если почта отсутствует
            }

            textViewName.setText(name);
            textViewEmail.setText(email);
        } else {
            // Если пользователь не авторизован, перенаправляем на страницу входа
            Toast.makeText(this, "Вы не вошли в систему!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, login_page_activity.class);
            startActivity(intent);
            finish(); // Закрываем эту активность
        }

        // Обработчик нажатия на кнопку выхода
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Выход из аккаунта через Firebase
                mAuth.signOut();
                Toast.makeText(ProfileActivity.this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();

                // Перенаправление на страницу входа
                Intent intent = new Intent(ProfileActivity.this, login_page_activity.class);
                startActivity(intent);
                finish(); // Закрываем эту активность
            }
        });
    }
}