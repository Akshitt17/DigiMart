package com.smarthome.stock.service;

import com.smarthome.stock.client.DeviceServiceClient;
import com.smarthome.stock.dto.StockRequest;
import com.smarthome.stock.dto.StockResponse;
import com.smarthome.stock.entity.StockEntity;
import com.smarthome.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map; 
import java.util.stream.Collectors;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private DeviceServiceClient deviceServiceClient;

    // Gateway ensures only ADMIN can call this
    public StockResponse addStock(StockRequest request) {
        // Verify device exists
        try {
            deviceServiceClient.getDeviceById(request.getDeviceId());
        } catch (Exception e) {
            throw new RuntimeException("Device not found with ID: " + request.getDeviceId());
        }

        StockEntity existingStock = stockRepository.findByDeviceId(request.getDeviceId()).orElse(null);
        
        if (existingStock != null) {
            // Update existing stock
            existingStock.setQuantity(existingStock.getQuantity() + request.getQuantity());
            StockEntity updatedStock = stockRepository.save(existingStock);
            return mapToResponse(updatedStock);
        } else {
            // Create new stock entry
            StockEntity newStock = new StockEntity(request.getDeviceId(), request.getQuantity());
            StockEntity savedStock = stockRepository.save(newStock);
            return mapToResponse(savedStock);
        }
    }

    // Gateway ensures only ADMIN can call this
    public StockResponse updateStock(Long deviceId, StockRequest request) {
        StockEntity stock = stockRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("Stock not found for device ID: " + deviceId));

        stock.setQuantity(request.getQuantity());
        StockEntity updatedStock = stockRepository.save(stock);
        return mapToResponse(updatedStock);
    }

    // Gateway ensures only ADMIN can call this
    @Transactional
    public String deleteStock(Long deviceId) {
        StockEntity stock = stockRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("Stock not found for device ID: " + deviceId));

        stockRepository.delete(stock);
        return "Stock deleted successfully";
    }

    // Public - anyone can view all stock
    public List<StockResponse> getAllStock() {
        return stockRepository.findAll()
                .stream()
                .map(this::mapToResponse)  // 🔄 Now includes device name fetching
                .collect(Collectors.toList());
    }

    // Public - anyone can view stock by device ID
    public StockResponse getStockByDeviceId(Long deviceId) {
        StockEntity stock = stockRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("Stock not found for device ID: " + deviceId));
        return mapToResponse(stock);
    }

//    // Public - view in-stock items
//    public List<StockResponse> getInStockItems() {
//        return stockRepository.findInStockItems()
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    // Gateway ensures only ADMIN can call this
//    public List<StockResponse> getOutOfStockItems() {
//        return stockRepository.findOutOfStockItems()
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    // Gateway ensures only ADMIN can call this
//    public List<StockResponse> getLowStockItems(Integer threshold) {
//        return stockRepository.findLowStockItems(threshold)
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    // Internal API - Check stock availability for orders
    public boolean isDeviceInStock(Long deviceId, Integer requiredQuantity) {
        StockEntity stock = stockRepository.findByDeviceId(deviceId).orElse(null);
        return stock != null && stock.getQuantity() >= requiredQuantity;
    }

    // Internal API - Reduce stock for orders
    @Transactional
    public StockResponse reduceStock(Long deviceId, Integer quantity) {
        StockEntity stock = stockRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("Stock not found for device ID: " + deviceId));

        if (stock.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + stock.getQuantity() + 
                                     ", Required: " + quantity);
        }

        stock.setQuantity(stock.getQuantity() - quantity);
        StockEntity updatedStock = stockRepository.save(stock);
        return mapToResponse(updatedStock);
    }

    // Internal API - Restore stock (for order cancellation)
    @Transactional
    public StockResponse restoreStock(Long deviceId, Integer quantity) {
        StockEntity stock = stockRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("Stock not found for device ID: " + deviceId));

        stock.setQuantity(stock.getQuantity() + quantity);
        StockEntity updatedStock = stockRepository.save(stock);
        return mapToResponse(updatedStock);
    }

    private String fetchDeviceName(Long deviceId) {
        try {
                       
            // Call your device service endpoint
            Object deviceResponse = deviceServiceClient.getDeviceById(deviceId).getBody();
            
            if (deviceResponse != null) {
                // Handle Object response - could be Map or DeviceResponse object
                if (deviceResponse instanceof Map) {
                    Map<String, Object> deviceMap = (Map<String, Object>) deviceResponse;
                    if (deviceMap.containsKey("deviceName")) {
                        String deviceName = deviceMap.get("deviceName").toString();
//                        System.out.println("✅ Device name fetched: " + deviceName);
                        return deviceName;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to fetch device name for deviceId " + deviceId + ": " + e.getMessage());
        }
        
        return "Device " + deviceId;  // Fallback
    }

    // 🆕 UPDATED MAPPING METHOD with device name
    private StockResponse mapToResponse(StockEntity stock) {
        String deviceName = fetchDeviceName(stock.getDeviceId());  // 🔄 Dynamic fetch
        
        return new StockResponse(
                stock.getStockId(),
                stock.getDeviceId(),
                     // 🆕 NOW INCLUDES DEVICE NAME
                stock.getQuantity(),
                stock.getUpdatedAt(),
                deviceName
        );
    }
}

