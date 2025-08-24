package com.smarthome.customer.service;

import com.smarthome.customer.dto.AddressRequest;
import com.smarthome.customer.dto.AddressResponse;
import com.smarthome.customer.dto.CustomerRequest;
import com.smarthome.customer.dto.CustomerResponse;
import com.smarthome.customer.entity.AddressEntity;
import com.smarthome.customer.entity.CustomerEntity;
import com.smarthome.customer.repository.AddressRepository;
import com.smarthome.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    // Gateway ensures proper authentication
    public CustomerResponse registerCustomer(CustomerRequest request, Long userId) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Customer with this email already exists");
        }

        CustomerEntity customer = new CustomerEntity(
                userId,
                request.getFullName(),
                request.getEmail(),
                request.getPhone()
        );

        CustomerEntity savedCustomer = customerRepository.save(customer);
        return mapToResponse(savedCustomer);
    }

    // Gateway ensures only authorized users access this
    public CustomerResponse getCustomerById(Long customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return mapToResponse(customer);
    }

    public CustomerResponse getCustomerByUserId(Long userId) {
        CustomerEntity customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return mapToResponse(customer);
    }

    // Gateway ensures only customer can update their own profile
    public CustomerResponse updateCustomer(Long customerId, CustomerRequest request) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        CustomerEntity updatedCustomer = customerRepository.save(customer);
        return mapToResponse(updatedCustomer);
    }

    @Transactional
    public String addAddress(Long customerId, AddressRequest request) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // If this is the first address or marked as default, set others to non-default
        if (request.getIsDefault() != null && request.getIsDefault()) {
            List<AddressEntity> existingAddresses = addressRepository.findByCustomerCustomerId(customerId);
            existingAddresses.forEach(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }

        AddressEntity address = new AddressEntity(
                customer,
                request.getStreetAddress(),
                request.getCity(),
                request.getState(),
                request.getPostalCode(),
                AddressEntity.AddressType.valueOf(request.getAddressType().toUpperCase())
        );
        
        List<AddressEntity> existingAddresses = addressRepository.findByCustomerCustomerId(customerId);
        address.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : existingAddresses.isEmpty());

        addressRepository.save(address);
        return "Address added successfully";
    }

    public List<AddressEntity> getCustomerAddresses(Long customerId) {
        return addressRepository.findByCustomerCustomerId(customerId);
    }
    
    public List<AddressResponse> getCustomerAddressesDto(Long customerId) {
        List<AddressEntity> addresses = addressRepository.findByCustomerCustomerId(customerId);
        
        return addresses.stream()
                .map(this::mapToAddressResponse)
                .collect(Collectors.toList());
    }

    
    private AddressResponse mapToAddressResponse(AddressEntity address) {
        return new AddressResponse(
                address.getAddressId(),
                address.getStreetAddress(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getAddressType().name(),
                address.getIsDefault(),
                address.getCreatedAt()
        );
    }
    private CustomerResponse mapToResponse(CustomerEntity customer) {
        return new CustomerResponse(
                customer.getCustomerId(),
                customer.getUserId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}
