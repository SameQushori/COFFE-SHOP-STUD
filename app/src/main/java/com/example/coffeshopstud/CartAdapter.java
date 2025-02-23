package com.example.coffeshopstud;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> items;

    public CartAdapter(List<CartItem> items) {
        this.items = items;
    }

    // Method to update the list of items
    public void updateItems(List<CartItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged(); // Notify the adapter of data changes
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_cart layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = items.get(position);

        // Bind data to views
        holder.productImage.setImageResource(item.getProduct().getImageResource());
        holder.productName.setText(item.getProduct().getName());
        holder.productPrice.setText(String.format("₽%d", item.getProduct().getPrice()));
        holder.quantity.setText(String.format("Количество: %d", item.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView quantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}