package com.smarthome.customer.controller;

import com.smarthome.customer.dto.AddressRequest;
import com.smarthome.customer.dto.AddressResponse; // ADD THIS IMPORT
import com.smarthome.customer.dto.CustomerRequest;
import com.smarthome.customer.dto.CustomerResponse;
//import com.smarthome.customer.entity.AddressEntity;
import com.smarthome.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
//@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Gateway ensures proper authentication
    @PostMapping("/register")
    public ResponseEntity<CustomerResponse> registerCustomer(
            @Valid @RequestBody CustomerRequest request,
            @RequestHeader(value = "X-User-Id") String userId) {
        
        try {
            Long userIdLong = Long.parseLong(userId);
            CustomerResponse response = customerService.registerCustomer(request, userIdLong);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Gateway handles authorization
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long customerId) {
        try {
            CustomerResponse customer = customerService.getCustomerById(customerId);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<CustomerResponse> getCustomerByUserId(@PathVariable Long userId) {
        try {
            CustomerResponse customer = customerService.getCustomerByUserId(userId);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Gateway ensures only customer can update their profile
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerRequest request) {
        
        try {
            CustomerResponse response = customerService.updateCustomer(customerId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/{customerId}/addresses")
    public ResponseEntity<String> addAddress(
            @PathVariable Long customerId,
            @Valid @RequestBody AddressRequest request) {
        
        try {
            String message = customerService.addAddress(customerId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding address");
        }
    }

   
    @GetMapping("/{customerId}/addresses")
    public ResponseEntity<List<AddressResponse>> getCustomerAddresses(@PathVariable Long customerId) {
        try {
            List<AddressResponse> addresses = customerService.getCustomerAddressesDto(customerId);
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
