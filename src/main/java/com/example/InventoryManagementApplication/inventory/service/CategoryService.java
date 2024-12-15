package com.example.InventoryManagementApplication.inventory.service;

import com.example.InventoryManagementApplication.inventory.dto.CategoryDto;
import com.example.InventoryManagementApplication.inventory.entity.Category;
import com.example.InventoryManagementApplication.inventory.exception.ResourceNotFoundException;
import com.example.InventoryManagementApplication.inventory.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDto createCategory(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        categoryRepository.save(category);
        dto.setId(category.getId());
        return dto;
    }

    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(c -> {
            CategoryDto dto = new CategoryDto();
            dto.setId(c.getId());
            dto.setName(c.getName());
            return dto;
        }).collect(Collectors.toList());
    }

    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setName(dto.getName());
        categoryRepository.save(category);
        dto.setId(category.getId());
        return dto;
    }

    public void deleteCategory(Long id) {
        if(!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}
