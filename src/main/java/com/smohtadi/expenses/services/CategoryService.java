package com.smohtadi.expenses.services;

import com.smohtadi.expenses.dtos.CategoryDto;
import com.smohtadi.expenses.models.Category;
import com.smohtadi.expenses.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> get() {
        return categoryRepository.findAll().stream().map(this::toDto).toList();
    }

    private CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId().toString(), category.getName());
    }
}
