package com.smarthome.stock.dto;

import java.time.LocalDateTime;

public class StockResponse {
    
    private Long stockId;
    private Long deviceId;
    private Integer quantity;
    private LocalDateTime updatedAt;
    private String status;
    private String deviceName;


    // Constructors
    public StockResponse() {}

    public StockResponse(Long stockId, Long deviceId, Integer quantity, LocalDateTime updatedAt,String deviceName) {
        this.stockId = stockId;
        this.deviceId = deviceId;
        this.quantity = quantity;
        this.updatedAt = updatedAt;
        this.deviceName= deviceName;
        this.status = quantity > 0 ? "IN_STOCK" : "OUT_OF_STOCK";
    }

    // Getters and Setters
    public Long getStockId() { return stockId; }
    public void setStockId(Long stockId) { this.stockId = stockId; }

    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { 
        this.quantity = quantity;
        this.status = quantity > 0 ? "IN_STOCK" : "OUT_OF_STOCK";
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
}
