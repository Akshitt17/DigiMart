package com.smarthome.stock.controller;

import com.smarthome.stock.dto.StockRequest;
import com.smarthome.stock.dto.StockResponse;
import com.smarthome.stock.service.StockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock")
//@CrossOrigin(origins = "*")
public class StockController {

    @Autowired
    private StockService stockService;

    // Gateway ensures only ADMIN reaches here
    @PostMapping
    public ResponseEntity<StockResponse> addStock(@Valid @RequestBody StockRequest request) {
        try {
            StockResponse response = stockService.addStock(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Gateway ensures only ADMIN reaches here
    @PutMapping("/device/{deviceId}")
    public ResponseEntity<StockResponse> updateStock(
            @PathVariable Long deviceId,
            @Valid @RequestBody StockRequest request) {
        
        try {
            StockResponse response = stockService.updateStock(deviceId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Gateway ensures only ADMIN reaches here
    @DeleteMapping("/device/{deviceId}")
    public ResponseEntity<String> deleteStock(@PathVariable Long deviceId) {
        try {
            String message = stockService.deleteStock(deviceId);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting stock");
        }
    }

    // Public - Gateway allows everyone
    @GetMapping
    public ResponseEntity<List<StockResponse>> getAllStock() {
        try {
            List<StockResponse> stockList = stockService.getAllStock();
            return ResponseEntity.ok(stockList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Public - Gateway allows everyone
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<StockResponse> getStockByDeviceId(@PathVariable Long deviceId) {
        try {
            StockResponse stock = stockService.getStockByDeviceId(deviceId);
            return ResponseEntity.ok(stock);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



//     Internal API - Check stock availability
    @GetMapping("/check/{deviceId}")
    public ResponseEntity<Boolean> checkStock(
            @PathVariable Long deviceId,
            @RequestParam Integer quantity) {
        try {
            boolean available = stockService.isDeviceInStock(deviceId, quantity);
            return ResponseEntity.ok(available);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    // Internal API - Reduce stock (for order processing)
    @PutMapping("/reduce/{deviceId}")
    public ResponseEntity<StockResponse> reduceStock(
            @PathVariable Long deviceId,
            @RequestParam Integer quantity) {
        try {
            StockResponse response = stockService.reduceStock(deviceId, quantity);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Internal API - Restore stock (for order cancellation)
    @PutMapping("/restore/{deviceId}")
    public ResponseEntity<StockResponse> restoreStock(
            @PathVariable Long deviceId,
            @RequestParam Integer quantity) {
        try {
            StockResponse response = stockService.restoreStock(deviceId, quantity);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
