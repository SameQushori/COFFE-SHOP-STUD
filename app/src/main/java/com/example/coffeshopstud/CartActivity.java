package com.example.coffeshopstud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private Cart cart;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView emptyCartTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Инициализация элементов интерфейса
        recyclerView = findViewById(R.id.recyclerViewCart);
        emptyCartTextView = findViewById(R.id.emptyCartTextView);

        // Получаем объект корзины из Intent
        cart = (Cart) getIntent().getSerializableExtra("cart");
        if (cart == null) {
            cart = new Cart(); // Если корзина пустая, создаем новую
        }

        // Настройка RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<CartItem> items = cart.getItems();
        cartAdapter = new CartAdapter(items);
        recyclerView.setAdapter(cartAdapter);

        // Обновляем UI корзины
        updateCartUI();

        // Обработка нажатия кнопки "Оформить заказ"
        Button checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> {
            if (!cart.getItems().isEmpty()) { // Проверяем, не пуста ли корзина
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                intent.putExtra("totalPrice", cart.getTotalPrice());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Корзина пуста", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработка нажатия кнопки "Назад"
        findViewById(R.id.backButton).setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, main_page_activity.class);
            startActivity(intent);
        });
    }

    // Метод для обновления UI корзины
    private void updateCartUI() {
        if (cartAdapter != null) {
            cartAdapter.updateItems(cart.getItems()); // Обновляем данные адаптера
        }

        // Обновляем текст общего количества
        TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
        if (totalPriceTextView != null) {
            totalPriceTextView.setText(String.format("Итого: %.2f ₽", cart.getTotalPrice()));
        }

        // Проверяем, пуста ли корзина
        if (cart.getItems().isEmpty()) {
            recyclerView.setVisibility(View.GONE); // Скрываем список товаров
            emptyCartTextView.setVisibility(View.VISIBLE); // Показываем сообщение
        } else {
            recyclerView.setVisibility(View.VISIBLE); // Показываем список товаров
            emptyCartTextView.setVisibility(View.GONE); // Скрываем сообщение
        }
    }
}