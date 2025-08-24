package com.smarthome.device.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "stock-service")
public interface StockServiceClient {
    
    @PostMapping("/api/v1/stock")
    ResponseEntity<Object> createInitialStock(@RequestBody Object stockRequest, 
                                              @RequestHeader("X-User-Id") String userId,
                                              @RequestHeader("X-User-Role") String role);
    
    @DeleteMapping("/api/v1/stock/device/{deviceId}")
    ResponseEntity<String> deleteStockByDevice(@PathVariable Long deviceId,
                                               @RequestHeader("X-User-Role") String role);
}
