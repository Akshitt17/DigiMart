package com.smarthome.customer.dto;

import java.time.LocalDateTime;

public class AddressResponse {
    private Long addressId;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String addressType;
    private Boolean isDefault;
    private LocalDateTime createdAt;

    // Default Constructor
    public AddressResponse() {}

    // Complete Constructor
    public AddressResponse(Long addressId, String streetAddress, String city, 
                          String state, String postalCode, String addressType,
                          Boolean isDefault, LocalDateTime createdAt) {
        this.addressId = addressId;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.addressType = addressType;
        this.isDefault = isDefault;
        this.createdAt = createdAt;
    }

    // Complete Getters and Setters
    public Long getAddressId() { 
        return addressId; 
    }
    
    public void setAddressId(Long addressId) { 
        this.addressId = addressId; 
    }

    public String getStreetAddress() { 
        return streetAddress; 
    }
    
    public void setStreetAddress(String streetAddress) { 
        this.streetAddress = streetAddress; 
    }

    public String getCity() { 
        return city; 
    }
    
    public void setCity(String city) { 
        this.city = city; 
    }

    public String getState() { 
        return state; 
    }
    
    public void setState(String state) { 
        this.state = state; 
    }

    public String getPostalCode() { 
        return postalCode; 
    }
    
    public void setPostalCode(String postalCode) { 
        this.postalCode = postalCode; 
    }

    public String getAddressType() { 
        return addressType; 
    }
    
    public void setAddressType(String addressType) { 
        this.addressType = addressType; 
    }

    public Boolean getIsDefault() { 
        return isDefault; 
    }
    
    public void setIsDefault(Boolean isDefault) { 
        this.isDefault = isDefault; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    @Override
    public String toString() {
        return "AddressResponse{" +
                "addressId=" + addressId +
                ", streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", addressType='" + addressType + '\'' +
                ", isDefault=" + isDefault +
                ", createdAt=" + createdAt +
                '}';
    }
}
