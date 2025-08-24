//package com.smarthome.device.service;
//
//import com.smarthome.device.client.StockServiceClient;
//import com.smarthome.device.dto.DeviceRequest;
//import com.smarthome.device.dto.DeviceResponse;
//import com.smarthome.device.entity.DeviceEntity;
//import com.smarthome.device.repository.DeviceRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//public class DeviceService {
//
//    @Autowired
//    private DeviceRepository deviceRepository;
//
//    @Autowired
//    private StockServiceClient stockServiceClient;
//
//    // Gateway ensures only ADMIN can call this
//    public DeviceResponse createDevice(DeviceRequest request, Long adminId) {
//        DeviceEntity device = new DeviceEntity(
//                request.getDeviceName(),
//                request.getDeviceType(),
//                request.getDescription(),
//                request.getPrice(),
//                adminId
//        );
//
//        DeviceEntity savedDevice = deviceRepository.save(device);
//
//        // Auto-create initial stock entry with 0 quantity
//        try {
//            Map<String, Object> stockRequest = new HashMap<>();
//            stockRequest.put("deviceId", savedDevice.getDeviceId());
//            stockRequest.put("quantity", 0);
//            
//            stockServiceClient.createInitialStock(stockRequest, 
//                                                  String.valueOf(adminId), 
//                                                  "ADMIN");
//        } catch (Exception e) {
//            System.err.println("Failed to create initial stock: " + e.getMessage());
//        }
//
//        return mapToResponse(savedDevice);
//    }
//
//    // Public - anyone can view devices
//    public List<DeviceResponse> getAllDevices() {
//        return deviceRepository.findAll()
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    // Public - anyone can view device details
//    public DeviceResponse getDeviceById(Long deviceId) {
//        DeviceEntity device = deviceRepository.findById(deviceId)
//                .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));
//        return mapToResponse(device);
//    }
//
//    // Gateway ensures only ADMIN can call this
//    public DeviceResponse updateDevice(Long deviceId, DeviceRequest request) {
//        DeviceEntity device = deviceRepository.findById(deviceId)
//                .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));
//
//        device.setDeviceName(request.getDeviceName());
//        device.setDeviceType(request.getDeviceType());
//        device.setDescription(request.getDescription());
//        device.setPrice(request.getPrice());
//
//        DeviceEntity updatedDevice = deviceRepository.save(device);
//        return mapToResponse(updatedDevice);
//    }
//
//    // Gateway ensures only ADMIN can call this
//    public String deleteDevice(Long deviceId) {
//        if (!deviceRepository.existsById(deviceId)) {
//            throw new RuntimeException("Device not found with id: " + deviceId);
//        }
//
//        // Delete associated stock first
//        try {
//            stockServiceClient.deleteStockByDevice(deviceId, "ADMIN");
//        } catch (Exception e) {
//            System.err.println("Failed to delete stock: " + e.getMessage());
//        }
//
//        deviceRepository.deleteById(deviceId);
//        return "Device deleted successfully";
//    }
//
//   
//
//    // Public - search devices
//    public List<DeviceResponse> searchDevices(String deviceName) {
//        return deviceRepository.findByDeviceNameContainingIgnoreCase(deviceName)
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
// 
//
//    private DeviceResponse mapToResponse(DeviceEntity device) {
//        return new DeviceResponse(
//                device.getDeviceId(),
//                device.getDeviceName(),
//                device.getDeviceType(),
//                device.getDescription(),
//                device.getPrice(),
//                device.getCreatedBy(),
//                device.getCreatedAt(),
//                device.getUpdatedAt()
//        );
//    }
//}
//
//
//




package com.smarthome.device.service;

import com.smarthome.device.client.StockServiceClient;
import com.smarthome.device.dto.DeviceRequest;
import com.smarthome.device.dto.DeviceResponse;
import com.smarthome.device.entity.DeviceEntity;
import com.smarthome.device.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private StockServiceClient stockServiceClient;

    // 🎯 SIMPLIFIED: Create device with image URL
    public DeviceResponse createDevice(DeviceRequest request, Long adminId) {
        System.out.println("🔄 Creating device: " + request.getDeviceName());
        
        DeviceEntity device = new DeviceEntity(
                request.getDeviceName(),
                request.getDeviceType(),
                request.getDescription(),
                request.getPrice(),
                request.getImageUrl(),  // 🆕 Store image URL directly
                adminId
        );

        DeviceEntity savedDevice = deviceRepository.save(device);
        System.out.println("✅ Device created with ID: " + savedDevice.getDeviceId());

        // Auto-create initial stock
        createInitialStock(savedDevice.getDeviceId(), adminId);

        return mapToResponse(savedDevice);
    }

    // 🏭 Create initial stock entry
    private void createInitialStock(Long deviceId, Long adminId) {
        try {
            Map<String, Object> stockRequest = new HashMap<>();
            stockRequest.put("deviceId", deviceId);
            stockRequest.put("quantity", 0);
            
            stockServiceClient.createInitialStock(stockRequest, 
                                                  String.valueOf(adminId), 
                                                  "ADMIN");
            System.out.println("✅ Initial stock created for device: " + deviceId);
        } catch (Exception e) {
            System.err.println("⚠️ Warning: Failed to create initial stock: " + e.getMessage());
        }
    }

    // 📋 GET ALL DEVICES
    public List<DeviceResponse> getAllDevices() {
        List<DeviceEntity> devices = deviceRepository.findAll();
        System.out.println("📱 Retrieved " + devices.size() + " devices from database");
        
        return devices.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 📱 GET SINGLE DEVICE
    public DeviceResponse getDeviceById(Long deviceId) {
        DeviceEntity device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));
        return mapToResponse(device);
    }

    // 🔄 UPDATE DEVICE (including image URL)
    public DeviceResponse updateDevice(Long deviceId, DeviceRequest request) {
        DeviceEntity device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));

        device.setDeviceName(request.getDeviceName());
        device.setDeviceType(request.getDeviceType());
        device.setDescription(request.getDescription());
        device.setPrice(request.getPrice());
        device.setImageUrl(request.getImageUrl()); // 🆕 Update image URL

        DeviceEntity updatedDevice = deviceRepository.save(device);
        return mapToResponse(updatedDevice);
    }

    // 🗑️ DELETE DEVICE
    public String deleteDevice(Long deviceId) {
        DeviceEntity device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));

        // Delete associated stock
        try {
            stockServiceClient.deleteStockByDevice(deviceId, "ADMIN");
            System.out.println("✅ Associated stock deleted for device: " + deviceId);
        } catch (Exception e) {
            System.err.println("⚠️ Warning: Failed to delete stock: " + e.getMessage());
        }

        deviceRepository.deleteById(deviceId);
        System.out.println("✅ Device deleted: " + deviceId);
        
        return "Device deleted successfully";
    }

    // 🔍 SEARCH DEVICES
    public List<DeviceResponse> searchDevices(String deviceName) {
        return deviceRepository.findByDeviceNameContainingIgnoreCase(deviceName)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🎯 MAP ENTITY TO RESPONSE
    private DeviceResponse mapToResponse(DeviceEntity device) {
        DeviceResponse response = new DeviceResponse(
                device.getDeviceId(),
                device.getDeviceName(),
                device.getDeviceType(),
                device.getDescription(),
                device.getPrice(),
                device.getImageUrl(),  // 🆕 Return image URL
                device.getCreatedBy(),
                device.getCreatedAt(),
                device.getUpdatedAt()
        );
        
        if (device.getImageUrl() != null) {
            System.out.println("🖼️ Device " + device.getDeviceName() + " has image URL: " + device.getImageUrl());
        }
        
        return response;
    }
}
