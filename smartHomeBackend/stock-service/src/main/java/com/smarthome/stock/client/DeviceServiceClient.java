package com.smarthome.stock.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "device-service")
public interface DeviceServiceClient {
    
    @GetMapping("/api/v1/devices/getBy/{deviceId}")
    ResponseEntity<Object> getDeviceById(@PathVariable Long deviceId);
}
