package com.example.coffeshopstud;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private int price;
    private int imageResource;

    public Product(String id, String name, int price, int imageResource) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
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

    public int getImageResource() {
        return imageResource;
    }
}