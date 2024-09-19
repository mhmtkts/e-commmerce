package com.example.e_commmerce;

import com.example.e_commmerce.controller.CartController;
import com.example.e_commmerce.dto.CartDTO;
import com.example.e_commmerce.dto.CartItemDTO;
import com.example.e_commmerce.dto.RemoveItemDTO;
import com.example.e_commmerce.entity.Cart;
import com.example.e_commmerce.entity.User;
import com.example.e_commmerce.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddToCart() {
        Long userId = 1L;
        Long productId = 2L;
        int quantity = 3;
        CartItemDTO cartItemDTO = new CartItemDTO(userId, productId, quantity);

        User user = new User();
        user.setId(userId);
        Cart cart = new Cart(user);
        cart.setId(1L);

        when(cartService.addItemToCart(userId, productId, quantity)).thenReturn(cart);

        ResponseEntity<CartDTO> response = cartController.addToCart(cartItemDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart.getId(), response.getBody().id());
        assertEquals(userId, response.getBody().userId());

        verify(cartService, times(1)).addItemToCart(userId, productId, quantity);
    }

    @Test
    void testRemoveFromCart() {
        Long userId = 1L;
        Long productId = 2L;
        RemoveItemDTO removeItemDTO = new RemoveItemDTO(userId, productId);

        ResponseEntity<Void> response = cartController.removeFromCart(removeItemDTO);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cartService, times(1)).removeItemFromCart(userId, productId);
    }

    @Test
    void testClearCart() {
        Long userId = 1L;
        Cart cart = new Cart();

        when(cartService.getCartByUserId(userId)).thenReturn(cart);

        ResponseEntity<Void> response = cartController.clearCart(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cartService, times(1)).getCartByUserId(userId);
        verify(cartService, times(1)).clearCart(cart);
    }

    @Test
    void testGetCart() {
        Long userId = 1L;
        Cart cart = new Cart();

        when(cartService.getCartByUserId(userId)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.getCart(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
        verify(cartService, times(1)).getCartByUserId(userId);
    }
}