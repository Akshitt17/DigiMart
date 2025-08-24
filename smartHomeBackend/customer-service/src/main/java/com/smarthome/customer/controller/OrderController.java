package com.smarthome.customer.controller;

import com.smarthome.customer.client.PaymentServiceClient;
import com.smarthome.customer.dto.CustomerResponse;
import com.smarthome.customer.dto.OrderRequest;
import com.smarthome.customer.dto.OrderResponse;
import com.smarthome.customer.service.CustomerService;
import com.smarthome.customer.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")

public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CustomerService customerService;
    

    @Autowired
    private PaymentServiceClient paymentServiceClient;
    

 
   
    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request,
            @RequestHeader(value = "X-User-Id") String userId) {
        
        try {
            // Get customer
            CustomerResponse customer = customerService.getCustomerByUserId(Long.parseLong(userId));
            
            // Create order
            OrderResponse response = orderService.createOrder(request, customer.getCustomerId());
            
            // 🚀 AUTO PAYMENT PROCESSING + INVOICE GENERATION
            try {
                Map<String, Object> paymentRequest = new HashMap<>();
                paymentRequest.put("orderId", response.getOrderId());
                paymentRequest.put("customerId", response.getCustomerId());
                paymentRequest.put("totalAmount", response.getTotalAmount());
                
                paymentServiceClient.processPayment(paymentRequest, "CUSTOMER");
                System.out.println("✅ Payment processed and invoice generated for order: " + response.getOrderId());
                
            } catch (Exception e) {
                System.err.println("❌ Payment processing failed: " + e.getMessage());
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Gateway handles customer/admin access
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getCustomerOrders(@PathVariable Long customerId) {
        try {
            List<OrderResponse> orders = orderService.getCustomerOrders(customerId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Long orderId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Role") String role) {
        
        try {
            Long customerId = null;
            if ("CUSTOMER".equals(role) && userId != null) {
                CustomerResponse customer = customerService.getCustomerByUserId(Long.parseLong(userId));
                customerId = customer.getCustomerId();
            }
            OrderResponse order = orderService.getOrderById(orderId, customerId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Gateway ensures only ADMIN can access this
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        try {
            List<OrderResponse> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Gateway ensures only ADMIN can update order status
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        
        try {
            OrderResponse response = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}