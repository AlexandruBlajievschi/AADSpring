package com.example.aadbackspring.service;

import com.example.aadbackspring.model.Category;
import com.example.aadbackspring.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category updateCategory(Long id, Category newCategoryData) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(newCategoryData.getName());
            category.setDescription(newCategoryData.getDescription());
            return categoryRepository.save(category);
        }).orElse(null);
    }

    public boolean deleteCategory(Long id) {
        return categoryRepository.findById(id).map(category -> {
            categoryRepository.delete(category);
            return true;
        }).orElse(false);
    }

    // For getting a category with its articles (if needed)
    public Optional<Category> getCategoryWithArticles(Long id) {
        return categoryRepository.findById(id);
    }
}
