package com.example.e_commmerce.service;

import com.example.e_commmerce.entity.Cart;

public interface CartService {
    Cart getCartByUserId(Long userId);
    Cart addItemToCart(Long userId, Long productId, Integer quantity);
    void removeItemFromCart(Long userId, Long productId);
    void clearCart(Cart cart);
}
