package com.example.coffeshopstud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class login_page_activity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_activity);

        mAuth = FirebaseAuth.getInstance();

        // Инициализация элементов интерфейса
        EditText editTextEmail = findViewById(R.id.log_editTextText); // Email
        EditText editTextPassword = findViewById(R.id.log_editTextText1); // Пароль
        Button startButton = findViewById(R.id.log_button); // Кнопка "Войти"
        TextView registerTextView = findViewById(R.id.log_textView9); // Текст "Зарегистрироваться"

        // Обработчик нажатия на кнопку "Войти"
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login_page_activity.this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Вход пользователя через Firebase
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(login_page_activity.this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(login_page_activity.this, "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(login_page_activity.this, main_page_activity.class);
                                startActivity(intent);
                                finish(); // Закрываем текущую активность
                            } else {
                                Toast.makeText(login_page_activity.this, "Ошибка входа: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
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