package com.smarthome.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvoiceResponse {
    private Long invoiceId;
    private Long orderId;
    private Long customerId;
    private String invoiceNumber;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    public InvoiceResponse(Long invoiceId, Long orderId, Long customerId, 
                          String invoiceNumber, BigDecimal totalAmount, LocalDateTime createdAt) {
        this.invoiceId = invoiceId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.invoiceNumber = invoiceNumber;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}