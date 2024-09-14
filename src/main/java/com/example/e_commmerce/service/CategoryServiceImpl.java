package com.example.e_commmerce.service;

import com.example.e_commmerce.dto.CategoryDTO;
import com.example.e_commmerce.entity.Category;
import com.example.e_commmerce.exceptions.ApiException;
import com.example.e_commmerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ApiException("Category not found: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.findByName(categoryDTO.name()).isPresent()) {
            throw new ApiException("Category already exists: " + categoryDTO.name(), HttpStatus.CONFLICT);
        }
        Category category = new Category();
        category.setName(categoryDTO.name());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = getCategoryById(id);
        if (!category.getName().equals(categoryDTO.name()) &&
                categoryRepository.findByName(categoryDTO.name()).isPresent()) {
            throw new ApiException("Category name already exists: " + categoryDTO.name(), HttpStatus.CONFLICT);
        }
        category.setName(categoryDTO.name());
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ApiException("Category not found: " + id, HttpStatus.NOT_FOUND);
        }
        categoryRepository.deleteById(id);
    }
}
