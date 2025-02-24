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

        recyclerView = findViewById(R.id.recyclerViewCart);
        emptyCartTextView = findViewById(R.id.emptyCartTextView);

        cart = (Cart) getIntent().getSerializableExtra("cart");
        if (cart == null) {
            cart = new Cart();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cart.getItems());
        recyclerView.setAdapter(cartAdapter);

        updateCartUI();

        Button checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> {
            if (!cart.getItems().isEmpty()) {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                intent.putExtra("totalPrice", cart.getTotalPrice());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Корзина пуста", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.backButton).setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, main_page_activity.class);
            startActivity(intent);
        });
    }

    private void updateCartUI() {
        if (cartAdapter != null) {
            cartAdapter.updateItems(cart.getItems());
        }

        TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
        if (totalPriceTextView != null) {
            totalPriceTextView.setText(String.format("Итого: %.2f ₽", cart.getTotalPrice()));
        }

        if (cart.getItems().isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyCartTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyCartTextView.setVisibility(View.GONE);
        }
    }
}