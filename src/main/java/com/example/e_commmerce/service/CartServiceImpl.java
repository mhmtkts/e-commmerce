package com.example.e_commmerce.service;

import com.example.e_commmerce.entity.Cart;
import com.example.e_commmerce.entity.CartItem;
import com.example.e_commmerce.entity.Product;
import com.example.e_commmerce.entity.User;
import com.example.e_commmerce.exceptions.ApiException;
import com.example.e_commmerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           UserService userService,
                           ProductService productService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("Cart not found for user: " + userId, HttpStatus.NOT_FOUND));
    }

    @Override
    public Cart addItemToCart(Long userId, Long productId, Integer quantity) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElse(new Cart(user));

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(cart, product, quantity);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItemFromCart(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
