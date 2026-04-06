package com.example.jpmart.utils;

import com.example.jpmart.models.CartItem;
import com.example.jpmart.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private List<CartItem> cartItems = new ArrayList<>();

    private CartManager() {}

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addProduct(Product product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(product, 1));
    }

    public void increaseQuantity(int position) {
        if (position >= 0 && position < cartItems.size()) {
            CartItem item = cartItems.get(position);
            if (item.getQuantity() < item.getProduct().getQuantity()) {
                item.setQuantity(item.getQuantity() + 1);
            }
        }
    }

    public boolean canIncrease(int position) {
        if (position >= 0 && position < cartItems.size()) {
            CartItem item = cartItems.get(position);
            return item.getQuantity() < item.getProduct().getQuantity();
        }
        return false;
    }

    public void decreaseQuantity(int position) {
        if (position >= 0 && position < cartItems.size()) {
            CartItem item = cartItems.get(position);
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            }
        }
    }

    public void removeItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
        }
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        return total;
    }

    public int getItemCount() {
        return cartItems.size();
    }

    public void clear() {
        cartItems.clear();
    }
}
