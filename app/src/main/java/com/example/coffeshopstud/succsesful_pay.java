package com.example.coffeshopstud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class succsesful_pay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succsesful_pay); // Убедитесь, что это имя вашего XML-файла

        // Инициализация кнопки "Вернуться"
        Button backButton = findViewById(R.id.succs_pay_button);

        // Обработка нажатия кнопки
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на главную страницу
                Intent intent = new Intent(succsesful_pay.this, main_page_activity.class);
                startActivity(intent);
                finish(); // Закрываем текущую активность
            }
        });
    }
}