package com.example.coffeshopstud;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private int price;
    private String imageUrl; // Изменено на String для URL-адреса

    public Product(String id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() { // Метод для получения URL-адреса
        return imageUrl;
    }
}