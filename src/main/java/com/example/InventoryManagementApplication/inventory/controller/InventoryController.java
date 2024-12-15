package com.example.InventoryManagementApplication.inventory.controller;

import com.example.InventoryManagementApplication.inventory.dto.ProductDto;
import com.example.InventoryManagementApplication.inventory.dto.StockUpdateRequest;
import com.example.InventoryManagementApplication.inventory.service.InventoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add/{productId}")
    public void addStock(@PathVariable Long productId, @Valid @RequestBody StockUpdateRequest request) {
        inventoryService.addStock(productId, request);
    }

    @PostMapping("/deduct/{productId}")
    public void deductStock(@PathVariable Long productId, @Valid @RequestBody StockUpdateRequest request) {
        inventoryService.deductStock(productId, request);
    }

    @GetMapping("/stock-level/{productId}")
    public int getStockLevel(@PathVariable Long productId) {
        return inventoryService.getStockLevel(productId);
    }

    @GetMapping("/low-stock")
    public List<ProductDto> getLowStockProducts(@RequestParam(defaultValue = "10") int threshold) {
        return inventoryService.getLowStockProducts(threshold);
    }
}
