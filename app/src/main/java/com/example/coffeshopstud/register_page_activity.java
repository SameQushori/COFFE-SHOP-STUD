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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class register_page_activity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page_activity);

        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        EditText editTextName = findViewById(R.id.editTextName); // Name
        EditText editTextEmail = findViewById(R.id.editTextEmail); // Email
        EditText editTextPassword = findViewById(R.id.editTextPassword); // Password
        Button startButton = findViewById(R.id.button); // Register Button
        TextView loginTextView = findViewById(R.id.textView9); // Login Link

        // Handle registration button click
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (validateInputs(name, email, password)) {
                    // Create user in Firebase
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(register_page_activity.this, task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        // Обновляем профиль пользователя с именем
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name) // Устанавливаем имя
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        Toast.makeText(register_page_activity.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(register_page_activity.this, main_page_activity.class);
                                                        startActivity(intent);
                                                        finish(); // Close this activity
                                                    } else {
                                                        Toast.makeText(register_page_activity.this, "Ошибка обновления профиля: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else {
                                    Toast.makeText(register_page_activity.this, "Ошибка регистрации: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        // Handle login link click
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register_page_activity.this, login_page_activity.class);
                startActivity(intent);
            }
        });
    }

    // Input validation method
    private boolean validateInputs(String name, String email, String password) {
        if (name.isEmpty()) {
            Toast.makeText(this, "Введите имя!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Некорректный email!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidPassword(password)) {
            Toast.makeText(this, "Пароль должен содержать минимум 6 символов, включая буквы и цифры!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Email validation method
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    // Password validation method
    private boolean isValidPassword(String password) {
        return password.length() >= 6 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }
}