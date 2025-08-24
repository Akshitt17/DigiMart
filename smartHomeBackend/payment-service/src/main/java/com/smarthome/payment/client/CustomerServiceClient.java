package com.smarthome.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "customer-service")
public interface CustomerServiceClient {
    
    @GetMapping("/api/v1/orders/customer/{customerId}")
    ResponseEntity<List<Object>> getCustomerOrders(@PathVariable Long customerId);
    
    @PutMapping("/api/v1/orders/{orderId}/status")
    ResponseEntity<Object> updateOrderStatus(@PathVariable Long orderId, 
                                           @RequestParam String status);
}