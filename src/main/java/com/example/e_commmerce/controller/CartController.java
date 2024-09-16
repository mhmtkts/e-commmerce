package com.example.e_commmerce.controller;

import com.example.e_commmerce.dto.CartDTO;
import com.example.e_commmerce.dto.CartItemDTO;
import com.example.e_commmerce.dto.RemoveItemDTO;
import com.example.e_commmerce.entity.Cart;
import com.example.e_commmerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private CartDTO convertToDTO(Cart cart) {
        return new CartDTO(cart.getId(), cart.getUser().getId());
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addToCart(@RequestBody CartItemDTO cartItemDTO) {
        // Burada productId'yi kullanarak ürün eklenir.
        Cart updatedCart = cartService.addItemToCart(cartItemDTO.userId(), cartItemDTO.productId(), cartItemDTO.quantity());
        CartDTO cartDTO = convertToDTO(updatedCart);
        return ResponseEntity.ok(cartDTO);
    }


    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromCart(@RequestBody RemoveItemDTO removeItemDTO) {
        cartService.removeItemFromCart(removeItemDTO.userId(), removeItemDTO.productId());
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        cartService.clearCart(cart);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }
}
