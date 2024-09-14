package com.example.e_commmerce.service;

import com.example.e_commmerce.dto.ProductDTO;
import com.example.e_commmerce.entity.Category;
import com.example.e_commmerce.entity.Product;
import com.example.e_commmerce.exceptions.ApiException;
import com.example.e_commmerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ApiException("Product not found: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public Product createProduct(ProductDTO productDTO) {
        Category category = categoryService.getCategoryById(productDTO.category().id());
        Product product = new Product();
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setPictureUrl(productDTO.pictureUrl());
        product.setCategory(category);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product product = getProductById(id);
        Category category = categoryService.getCategoryById(productDTO.category().id());

        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setPictureUrl(productDTO.pictureUrl());
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ApiException("Product not found: " + id, HttpStatus.NOT_FOUND);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}
