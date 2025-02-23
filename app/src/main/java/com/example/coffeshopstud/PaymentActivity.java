package com.example.coffeshopstud;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Получаем данные
        String address = getIntent().getStringExtra("address");
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        // Отображаем данные
        TextView addressTextView = findViewById(R.id.addressTextView);
        addressTextView.setText(String.format("Адрес доставки: %s", address));

        TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
        totalPriceTextView.setText(String.format("К оплате: %.2f ₽", totalPrice));
    }
}