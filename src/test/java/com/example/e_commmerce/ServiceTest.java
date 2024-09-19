package com.example.e_commmerce;

import com.example.e_commmerce.entity.Cart;
import com.example.e_commmerce.entity.CartItem;
import com.example.e_commmerce.entity.Product;
import com.example.e_commmerce.entity.User;
import com.example.e_commmerce.exceptions.ApiException;
import com.example.e_commmerce.repository.CartRepository;
import com.example.e_commmerce.service.CartServiceImpl;
import com.example.e_commmerce.service.ProductService;
import com.example.e_commmerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCartByUserId() {
        Long userId = 1L;
        Cart cart = new Cart();
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByUserId(userId);

        assertEquals(cart, result);
        verify(cartRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetCartByUserIdNotFound() {
        Long userId = 1L;
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> cartService.getCartByUserId(userId));

        assertEquals("Cart not found for user: " + userId, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testAddItemToCart() {
        Long userId = 1L;
        Long productId = 2L;
        Integer quantity = 3;

        User user = new User();
        user.setId(userId);
        Product product = new Product();
        product.setId(productId);
        Cart cart = new Cart(user);

        when(userService.getUserById(userId)).thenReturn(user);
        when(productService.getProductById(productId)).thenReturn(product);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart result = cartService.addItemToCart(userId, productId, quantity);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(product, result.getItems().get(0).getProduct());
        assertEquals(quantity, result.getItems().get(0).getQuantity());

        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testRemoveItemFromCart() {
        Long userId = 1L;
        Long productId = 2L;

        User user = new User();
        user.setId(userId);
        Product product = new Product();
        product.setId(productId);
        Cart cart = new Cart(user);
        cart.getItems().add(new CartItem(cart, product, 1));

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        cartService.removeItemFromCart(userId, productId);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testClearCart() {
        Cart cart = new Cart();
        cart.getItems().add(new CartItem());
        cart.getItems().add(new CartItem());

        cartService.clearCart(cart);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository, times(1)).save(cart);
    }
}