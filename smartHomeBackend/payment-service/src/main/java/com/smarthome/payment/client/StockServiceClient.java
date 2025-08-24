package com.smarthome.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "stock-service")
public interface StockServiceClient {
    
    @GetMapping("/api/v1/stock")
    ResponseEntity<List<Object>> getAllStock();
    
    @GetMapping("/api/v1/stock/low-stock")
    ResponseEntity<List<Object>> getLowStock(@RequestParam int threshold);
}