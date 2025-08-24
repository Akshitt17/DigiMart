package com.smarthome.payment.dto;

import java.math.BigDecimal;

public class PaymentRequest {
    private Long orderId;
    private Long customerId;
    private BigDecimal totalAmount;
    
    public PaymentRequest() {}
    
    public PaymentRequest(Long orderId, Long customerId, BigDecimal totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}