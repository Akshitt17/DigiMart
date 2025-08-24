package com.smarthome.customer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "payment-service")
public interface PaymentServiceClient {
    
    @PostMapping("/api/v1/payments/process")
    ResponseEntity<Object> processPayment(@RequestBody Map<String, Object> paymentRequest,
                                        @RequestHeader("X-User-Role") String role);
}