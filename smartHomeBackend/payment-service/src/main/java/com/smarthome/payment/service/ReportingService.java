package com.smarthome.payment.service;

import com.smarthome.payment.client.CustomerServiceClient;
import com.smarthome.payment.client.StockServiceClient;
import com.smarthome.payment.dto.InvoiceResponse;
import com.smarthome.payment.entity.InvoiceEntity;
import com.smarthome.payment.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportingService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerServiceClient customerServiceClient;

    @Autowired
    private StockServiceClient stockServiceClient;

    // Get customer orders (delegate to customer service)
    public List<Object> getCustomerOrders(Long customerId) {
        try {
            return customerServiceClient.getCustomerOrders(customerId).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch customer orders: " + e.getMessage());
        }
    }

    // Get customer invoices
    public List<InvoiceResponse> getCustomerInvoices(Long customerId) {
        return invoiceRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToInvoiceResponse)
                .collect(Collectors.toList());
    }

    // Get invoice by order ID
    public InvoiceResponse getInvoiceByOrderId(Long orderId) {
        InvoiceEntity invoice = invoiceRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Invoice not found for order: " + orderId));
        return mapToInvoiceResponse(invoice);
    }

    // Admin: Get all stock (delegate to stock service)
    public List<Object> getAllStock() {
        try {
            return stockServiceClient.getAllStock().getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch stock data: " + e.getMessage());
        }
    }


    private InvoiceResponse mapToInvoiceResponse(InvoiceEntity invoice) {
        return new InvoiceResponse(
                invoice.getInvoiceId(),
                invoice.getOrderId(),
                invoice.getCustomerId(),
                invoice.getInvoiceNumber(),
                invoice.getTotalAmount(),
                invoice.getCreatedAt()
        );
    }
}
