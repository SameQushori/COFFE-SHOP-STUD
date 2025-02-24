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

        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Get current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Set user data
            String name = user.getDisplayName(); // Display name
            String email = user.getEmail();
            if (name == null || name.isEmpty()) {
                name = "Имя не указано"; // Fallback if name is not set
            }
            if (email == null || email.isEmpty()) {
                email = "Почта не указана"; // Fallback if email is not set
            }
            textViewName.setText(name);
            textViewEmail.setText(email);
        } else {
            // If user is not logged in, redirect to login page
            Toast.makeText(this, "Вы не вошли в систему!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, login_page_activity.class);
            startActivity(intent);
            finish(); // Close this activity
        }

        // Back button handler
        findViewById(R.id.buttonBack).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, main_page_activity.class);
            startActivity(intent);
        });

        // Logout button handler
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(ProfileActivity.this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileActivity.this, login_page_activity.class);
                startActivity(intent);
                finish(); // Close this activity
            }
        });
    }
}