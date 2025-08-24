//package com.smarthome.device.entity;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Positive;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "device")
//@EntityListeners(AuditingEntityListener.class)
//public class DeviceEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "device_id")
//    private Long deviceId;
//
//    @NotBlank(message = "Device name is required")
//    @Column(name = "device_name", nullable = false, length = 100)
//    private String deviceName;
//
//    @Column(name = "device_type", length = 50)
//    private String deviceType;
//
//    @Column(name = "description", columnDefinition = "TEXT")
//    private String description;
//
//    @NotNull(message = "Price is required")
//    @Positive(message = "Price must be positive")
//    @Column(name = "price", nullable = false, precision = 10, scale = 2)
//    private BigDecimal price;
//    
//    
//
//    @NotNull(message = "Created by is required")
//    @Column(name = "created_by", nullable = false)
//    private Long createdBy;
//
//    @CreatedDate
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    // Constructors
//    public DeviceEntity() {}
//
//    public DeviceEntity(String deviceName, String deviceType, String description, 
//                       BigDecimal price, Long createdBy) {
//        this.deviceName = deviceName;
//        this.deviceType = deviceType;
//        this.description = description;
//        this.price = price;
//        this.createdBy = createdBy;
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
//
//
//



package com.smarthome.device.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "device")
@EntityListeners(AuditingEntityListener.class)
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Long deviceId;

    @NotBlank(message = "Device name is required")
    @Column(name = "device_name", nullable = false, length = 100)
    private String deviceName;

    @Column(name = "device_type", length = 50)
    private String deviceType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // 🆕 ONLY IMAGE URL FIELD
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @NotNull(message = "Created by is required")
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public DeviceEntity() {}

    public DeviceEntity(String deviceName, String deviceType, String description, 
                       BigDecimal price, Long createdBy) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.description = description;
        this.price = price;
        this.createdBy = createdBy;
    }

    // 🆕 CONSTRUCTOR WITH IMAGE URL
    public DeviceEntity(String deviceName, String deviceType, String description, 
                       BigDecimal price, String imageUrl, Long createdBy) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.createdBy = createdBy;
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




