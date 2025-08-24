package com.smarthome.customer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "stock-service")
public interface StockServiceClient {
    
    @GetMapping("/api/v1/stock/check/{deviceId}")
    ResponseEntity<Boolean> checkStock(@PathVariable Long deviceId, 
                                     @RequestParam Integer quantity);
    
    @PutMapping("/api/v1/stock/reduce/{deviceId}")
    ResponseEntity<Object> reduceStock(@PathVariable Long deviceId, 
                                     @RequestParam Integer quantity);
    
    @PutMapping("/api/v1/stock/restore/{deviceId}")
    ResponseEntity<Object> restoreStock(@PathVariable Long deviceId, 
                                      @RequestParam Integer quantity);
}