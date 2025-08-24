package com.smarthome.stock.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class StockRequest {
    
    @NotNull(message = "Device ID is required")
    private Long deviceId;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    // Constructors
    public StockRequest() {}

    public StockRequest(Long deviceId, Integer quantity) {
        this.deviceId = deviceId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
