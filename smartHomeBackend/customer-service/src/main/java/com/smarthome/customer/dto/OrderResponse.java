package com.smarthome.customer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long orderId;
    private Long customerId;
    private Long deliveryAddressId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String status;
    private List<OrderItemResponse> orderItems;

    public OrderResponse(Long orderId, Long customerId, Long deliveryAddressId,
                        LocalDateTime orderDate, BigDecimal totalAmount, String status,
                        List<OrderItemResponse> orderItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.deliveryAddressId = deliveryAddressId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderItems = orderItems;
    }
    
    public OrderResponse() {}

    // Getters and setters...
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getDeliveryAddressId() { return deliveryAddressId; }
    public void setDeliveryAddressId(Long deliveryAddressId) { this.deliveryAddressId = deliveryAddressId; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<OrderItemResponse> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemResponse> orderItems) { this.orderItems = orderItems; }

    public static class OrderItemResponse {
        private Long orderItemId;
        private Long deviceId;
        private Integer quantity;
        private BigDecimal price;
        private String deviceName;

        public OrderItemResponse(Long orderItemId, Long deviceId, Integer quantity, BigDecimal price,String deviceName) {
            this.orderItemId = orderItemId;
            this.deviceId = deviceId;
            this.quantity = quantity;
            this.price = price;
            this.deviceName = deviceName;
            
        }

        // Getters and setters...
        public Long getOrderItemId() { return orderItemId; }
        public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }

        public Long getDeviceId() { return deviceId; }
        public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        
        public String getDeviceName() { return deviceName; }
        public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    }
}
