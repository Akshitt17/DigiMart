package com.smarthome.payment.controller;

import com.smarthome.payment.dto.InvoiceResponse;
import com.smarthome.payment.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
//@CrossOrigin(origins = "*")
public class ReportingController {

    @Autowired
    private ReportingService reportingService;

    // Customer can see their previous orders (delegates to customer service)
    @GetMapping("/customer/{customerId}/orders")
    public ResponseEntity<List<Object>> getCustomerOrders(
            @PathVariable Long customerId,
            @RequestHeader(value = "X-User-Role") String role) {
        
        try {
            List<Object> orders = reportingService.getCustomerOrders(customerId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Customer can see their invoices
    @GetMapping("/customer/{customerId}/invoices")
    public ResponseEntity<List<InvoiceResponse>> getCustomerInvoices(
            @PathVariable Long customerId,
            @RequestHeader(value = "X-User-Role") String role) {
        
        try {
            List<InvoiceResponse> invoices = reportingService.getCustomerInvoices(customerId);
            return ResponseEntity.ok(invoices);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Get invoice by order ID
    @GetMapping("/invoice/order/{orderId}")
    public ResponseEntity<InvoiceResponse> getInvoiceByOrderId(@PathVariable Long orderId) {
        try {
            InvoiceResponse invoice = reportingService.getInvoiceByOrderId(orderId);
            return ResponseEntity.ok(invoice);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // Admin can view all stock (delegates to stock service)
    @GetMapping("/admin/stock")
    public ResponseEntity<List<Object>> getAllStock(
            @RequestHeader(value = "X-User-Role") String role) {
        
        try {
            List<Object> stockData = reportingService.getAllStock();
            return ResponseEntity.ok(stockData);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


}