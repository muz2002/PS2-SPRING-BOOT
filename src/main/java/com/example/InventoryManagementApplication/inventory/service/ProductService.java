package com.example.InventoryManagementApplication.inventory.service;

import com.example.InventoryManagementApplication.inventory.dto.ProductDto;
import com.example.InventoryManagementApplication.inventory.entity.Category;
import com.example.InventoryManagementApplication.inventory.entity.Inventory;
import com.example.InventoryManagementApplication.inventory.entity.Product;
import com.example.InventoryManagementApplication.inventory.exception.ResourceNotFoundException;
import com.example.InventoryManagementApplication.inventory.repository.CategoryRepository;
import com.example.InventoryManagementApplication.inventory.repository.ProductRepository;
import com.example.InventoryManagementApplication.inventory.repository.InventoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    public ProductDto createProduct(ProductDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 0);
        product.setInventory(inventory);

        productRepository.save(product);
        return convertToDto(product);
    }

    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return convertToDto(product);
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);
        if(dto.getQuantity() != null) {
            product.getInventory().setQuantity(dto.getQuantity());
        }
        productRepository.save(product);
        return convertToDto(product);
    }

    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public List<ProductDto> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCategoryId(product.getCategory().getId());
        dto.setQuantity(product.getInventory() != null ? product.getInventory().getQuantity() : 0);
        return dto;
    }
}
