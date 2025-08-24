//package com.smarthome.device.dto;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Positive;
//
//import java.math.BigDecimal;
//
//public class DeviceRequest {
//    
//    @NotBlank(message = "Device name is required")
//    private String deviceName;
//    
//    private String deviceType;
//    
//    private String description;
//    
//    @NotNull(message = "Price is required")
//    @Positive(message = "Price must be positive")
//    private BigDecimal price;
//
//    // Constructors
//    public DeviceRequest() {}
//
//    public DeviceRequest(String deviceName, String deviceType, String description, BigDecimal price) {
//        this.deviceName = deviceName;
//        this.deviceType = deviceType;
//        this.description = description;
//        this.price = price;
//    }
//
//    // Getters and Setters
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
//}



package com.smarthome.device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class DeviceRequest {
    
    @NotBlank(message = "Device name is required")
    private String deviceName;
    
    private String deviceType;
    
    private String description;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    // 🆕 ADD IMAGE URL FIELD
    private String imageUrl;

    // Constructors
    public DeviceRequest() {}

    public DeviceRequest(String deviceName, String deviceType, String description, 
                        BigDecimal price, String imageUrl) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
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
}
