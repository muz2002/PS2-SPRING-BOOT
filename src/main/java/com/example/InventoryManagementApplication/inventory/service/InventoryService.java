package com.example.InventoryManagementApplication.inventory.service;

import com.example.InventoryManagementApplication.inventory.dto.ProductDto;
import com.example.InventoryManagementApplication.inventory.dto.StockUpdateRequest;
import com.example.InventoryManagementApplication.inventory.entity.Inventory;
import com.example.InventoryManagementApplication.inventory.entity.Product;
import com.example.InventoryManagementApplication.inventory.exception.ResourceNotFoundException;
import com.example.InventoryManagementApplication.inventory.repository.InventoryRepository;
import com.example.InventoryManagementApplication.inventory.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private NotificationService notificationService;

    public void addStock(Long productId, StockUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Inventory inventory = product.getInventory();
        inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
        inventoryRepository.save(inventory);
    }

    public void deductStock(Long productId, StockUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Inventory inventory = product.getInventory();
        if (inventory.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
        inventoryRepository.save(inventory);
    }

    public int getStockLevel(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return product.getInventory().getQuantity();
    }

    public List<ProductDto> getLowStockProducts(int threshold) {
        List<ProductDto> lowStockProducts = inventoryRepository.findByQuantityLessThan(threshold).stream().map(inv -> {
            Product p = inv.getProduct();
            ProductDto dto = new ProductDto();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setPrice(p.getPrice());
            dto.setCategoryId(p.getCategory().getId());
            dto.setQuantity(inv.getQuantity());
            return dto;
        }).collect(Collectors.toList());

        // Send notification if there are low-stock products
        notificationService.sendLowStockNotification(lowStockProducts);

        return lowStockProducts;
    }
}