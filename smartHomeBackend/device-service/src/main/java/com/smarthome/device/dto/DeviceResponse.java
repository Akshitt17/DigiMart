//package com.smarthome.device.dto;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//public class DeviceResponse {
//    
//    private Long deviceId;
//    private String deviceName;
//    private String deviceType;
//    private String description;
//    private BigDecimal price;
//    private Long createdBy;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//
//    // Constructors
//    public DeviceResponse() {}
//
//    public DeviceResponse(Long deviceId, String deviceName, String deviceType, 
//                         String description, BigDecimal price, Long createdBy, 
//                         LocalDateTime createdAt, LocalDateTime updatedAt) {
//        this.deviceId = deviceId;
//        this.deviceName = deviceName;
//        this.deviceType = deviceType;
//        this.description = description;
//        this.price = price;
//        this.createdBy = createdBy;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }
//
//    // Getters and Setters
//    public Long getDeviceId() { return deviceId; }
//    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
//
//    public String getDeviceName() { return deviceName; }
//    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
//
//    public String getDeviceType() { return deviceType; }
//    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
//
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//
//    public BigDecimal getPrice() { return price; }
//    public void setPrice(BigDecimal price) { this.price = price; }
//
//    public Long getCreatedBy() { return createdBy; }
//    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
//
//    public LocalDateTime getCreatedAt() { return createdAt; }
//    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
//
//    public LocalDateTime getUpdatedAt() { return updatedAt; }
//    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
//}




package com.smarthome.device.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DeviceResponse {
    
    private Long deviceId;
    private String deviceName;
    private String deviceType;
    private String description;
    private BigDecimal price;
    private String imageUrl;  // 🆕 ONLY IMAGE URL FIELD
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public DeviceResponse() {}

    public DeviceResponse(Long deviceId, String deviceName, String deviceType, 
                         String description, BigDecimal price, String imageUrl, 
                         Long createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    // 🆕 IMAGE URL GETTER/SETTER
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
