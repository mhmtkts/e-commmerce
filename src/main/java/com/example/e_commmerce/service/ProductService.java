package com.example.e_commmerce.service;

import com.example.e_commmerce.dto.ProductDTO;
import com.example.e_commmerce.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product createProduct(ProductDTO productDTO);
    Product updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
    List<Product> getProductsByCategory(Long categoryId);
}
