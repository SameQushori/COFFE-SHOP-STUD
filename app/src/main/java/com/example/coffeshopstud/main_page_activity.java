package com.example.coffeshopstud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class main_page_activity extends AppCompatActivity implements ProductAdapter.OnAddToCartClickListener {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private FirebaseFirestore db;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_activity);

        // Initialize Cart
        cart = new Cart();

        // Set up RecyclerView for products
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        // Load products from Firebase Firestore
        loadProducts();

        // Set up Bottom Navigation Menu
        setupBottomNavigation();
    }

    private void loadProducts() {
        db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String id = document.getId();
                        String name = document.getString("name");
                        int price = document.getLong("price").intValue();
                        String image = document.getString("image");
                        int drawableResourceId = getImageResource(image);

                        // Create a Product object and add it to the list
                        Product product = new Product(id, name, price, drawableResourceId);
                        productList.add(product);
                    }

                    // Set up the ProductAdapter with the loaded products
                    productAdapter = new ProductAdapter(productList, this);
                    recyclerViewProducts.setAdapter(productAdapter);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Ошибка загрузки товаров", Toast.LENGTH_SHORT).show());
    }

    private int getImageResource(String imageName) {
        try {
            return getResources().getIdentifier(imageName, "drawable", getPackageName());
        } catch (Exception e) {
            return R.drawable.placeholder_image; // Fallback image if not found
        }
    }

    @Override
    public void onAddToCartClick(Product product, int quantity) {
        if (quantity > 0) {
            cart.addItem(product, quantity); // Add item to cart
            Toast.makeText(this, String.format("Добавлено: %s (%d шт)", product.getName(), quantity), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Введите корректное количество", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBottomNavigation() {
        ImageButton buttonProfile = findViewById(R.id.buttonProfile);
        ImageButton buttonFavorites = findViewById(R.id.buttonFavorites);
        ImageButton buttonCart = findViewById(R.id.buttonCart);

        // Navigate to Profile Activity
        buttonProfile.setOnClickListener(v -> {
            Intent intent = new Intent(main_page_activity.this, ProfileActivity.class); // Replace with actual ProfileActivity
            startActivity(intent);
        });

        // Navigate to Favorites Activity
        buttonFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(main_page_activity.this, FavoritesActivity.class); // Replace with actual FavoritesActivity
            startActivity(intent);
        });

        // Navigate to Cart Activity
        buttonCart.setOnClickListener(v -> {
            Intent intent = new Intent(main_page_activity.this, CartActivity.class);
            intent.putExtra("cart", cart); // Pass the cart object to CartActivity
            startActivity(intent);
        });
    }
}