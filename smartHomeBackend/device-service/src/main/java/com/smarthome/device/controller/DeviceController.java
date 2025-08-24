//package com.smarthome.device.controller;
//
//import com.smarthome.device.dto.DeviceRequest;
//import com.smarthome.device.dto.DeviceResponse;
//import com.smarthome.device.service.DeviceService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/devices")
//
//public class DeviceController {
//
//    @Autowired
//    private DeviceService deviceService;
//
//    // Gateway ensures only ADMIN reaches here
//    @PostMapping
//    public ResponseEntity<DeviceResponse> createDevice(
//            @Valid @RequestBody DeviceRequest request,
//            @RequestHeader(value = "X-User-Id") String userId) {
//        
//        try {
//            Long adminId = Long.parseLong(userId);
//            System.out.println("DEBUG - X-User-Id: " + adminId);
//
//            DeviceResponse response = deviceService.createDevice(request, adminId);
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//    }
//
//    // Public - Gateway allows everyone
//    @GetMapping("/getAll")
//    public ResponseEntity<List<DeviceResponse>> getAllDevices() {
//        try {
//            List<DeviceResponse> devices = deviceService.getAllDevices();
//            return ResponseEntity.ok(devices);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    // Public - Gateway allows everyone
//    @GetMapping("/getBy/{deviceId}")
//    public ResponseEntity<DeviceResponse> getDeviceById(@PathVariable Long deviceId) {
//        try {
//            DeviceResponse device = deviceService.getDeviceById(deviceId);
//            return ResponseEntity.ok(device);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    // Gateway ensures only ADMIN reaches here
//    @PutMapping("/{deviceId}")
//    public ResponseEntity<DeviceResponse> updateDevice(
//            @PathVariable Long deviceId,
//            @Valid @RequestBody DeviceRequest request) {
//        
//        try {
//            DeviceResponse response = deviceService.updateDevice(deviceId, request);
//            return ResponseEntity.ok(response);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//    }
//
//    // Gateway ensures only ADMIN reaches here
//    @DeleteMapping("/{deviceId}")
//    public ResponseEntity<String> deleteDevice(@PathVariable Long deviceId) {
//        try {
//            String message = deviceService.deleteDevice(deviceId);
//            return ResponseEntity.ok(message);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting device");
//        }
//    }
//
//  
//
//    // Public - Gateway allows everyone
//    @GetMapping("/search")
//    public ResponseEntity<List<DeviceResponse>> searchDevices(@RequestParam String name) {
//        try {
//            List<DeviceResponse> devices = deviceService.searchDevices(name);
//            return ResponseEntity.ok(devices);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//   
//}



package com.smarthome.device.controller;

import com.smarthome.device.dto.DeviceRequest;
import com.smarthome.device.dto.DeviceResponse;
import com.smarthome.device.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    // 🎯 SIMPLE: Create device with image URL (JSON only)
    @PostMapping
    public ResponseEntity<DeviceResponse> createDevice(
            @Valid @RequestBody DeviceRequest request,
            @RequestHeader(value = "X-User-Id") String userId) {
        
        try {
            Long adminId = Long.parseLong(userId);
            System.out.println("DEBUG - X-User-Id: " + adminId);

            DeviceResponse response = deviceService.createDevice(request, adminId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            System.err.println("❌ Device creation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Public - Gateway allows everyone
    @GetMapping("/getAll")
    public ResponseEntity<List<DeviceResponse>> getAllDevices() {
        try {
            List<DeviceResponse> devices = deviceService.getAllDevices();
            return ResponseEntity.ok(devices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Public - Gateway allows everyone
    @GetMapping("/getBy/{deviceId}")
    public ResponseEntity<DeviceResponse> getDeviceById(@PathVariable Long deviceId) {
        try {
            DeviceResponse device = deviceService.getDeviceById(deviceId);
            return ResponseEntity.ok(device);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Gateway ensures only ADMIN reaches here
    @PutMapping("/{deviceId}")
    public ResponseEntity<DeviceResponse> updateDevice(
            @PathVariable Long deviceId,
            @Valid @RequestBody DeviceRequest request) {
        
        try {
            DeviceResponse response = deviceService.updateDevice(deviceId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Gateway ensures only ADMIN reaches here
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<String> deleteDevice(@PathVariable Long deviceId) {
        try {
            String message = deviceService.deleteDevice(deviceId);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting device");
        }
    }

    // Public - Gateway allows everyone
    @GetMapping("/search")
    public ResponseEntity<List<DeviceResponse>> searchDevices(@RequestParam String name) {
        try {
            List<DeviceResponse> devices = deviceService.searchDevices(name);
            return ResponseEntity.ok(devices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

