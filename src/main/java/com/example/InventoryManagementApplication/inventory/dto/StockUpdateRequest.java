package com.example.InventoryManagementApplication.inventory.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockUpdateRequest {
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
