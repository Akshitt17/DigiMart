package com.smarthome.payment.service;

import com.smarthome.payment.client.CustomerServiceClient;
//import com.smarthome.payment.dto.PaymentRequest;
import com.smarthome.payment.dto.PaymentResponse;
import com.smarthome.payment.entity.InvoiceEntity;
import com.smarthome.payment.entity.PaymentEntity;
import com.smarthome.payment.repository.InvoiceRepository;
import com.smarthome.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerServiceClient customerServiceClient;

    // Process payment as called by customer service
    @Transactional
    public PaymentResponse processPayment(Map<String, Object> paymentRequest) {
        
        // Extract data from payment request
        Long orderId = Long.valueOf(paymentRequest.get("orderId").toString());
        Long customerId = Long.valueOf(paymentRequest.get("customerId").toString());
        BigDecimal amount = new BigDecimal(paymentRequest.get("totalAmount").toString());
        
        // Create payment record (always success for simplicity)
        PaymentEntity payment = new PaymentEntity(orderId, customerId, amount);
        PaymentEntity savedPayment = paymentRepository.save(payment);
        
        // Generate invoice automatically
        generateInvoice(orderId, customerId, amount);
        
        // Update order status to CONFIRMED in customer service
        try {
            customerServiceClient.updateOrderStatus(orderId, "CONFIRMED");
        } catch (Exception e) {
            System.err.println("Failed to update order status: " + e.getMessage());
        }
        
        return new PaymentResponse(
                savedPayment.getPaymentId(),
                savedPayment.getOrderId(),
                savedPayment.getCustomerId(),
                savedPayment.getAmount(),
                savedPayment.getPaymentStatus().name(),
                savedPayment.getCreatedAt()
        );
    }

    // Generate invoice automatically
    private void generateInvoice(Long orderId, Long customerId, BigDecimal amount) {
        
        // Check if invoice already exists
        if (invoiceRepository.findByOrderId(orderId).isPresent()) {
            return; // Invoice already generated
        }
        
        String invoiceNumber = generateInvoiceNumber();
        InvoiceEntity invoice = new InvoiceEntity(orderId, customerId, invoiceNumber, amount);
        invoiceRepository.save(invoice);
    }

    // Simple invoice number generation
    private String generateInvoiceNumber() {
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = invoiceRepository.count() + 1;
        return String.format("INV%s%04d", datePrefix, count);
    }

    // Get payment by order ID
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));
        
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getCustomerId(),
                payment.getAmount(),
                payment.getPaymentStatus().name(),
                payment.getCreatedAt()
        );
    }
}
