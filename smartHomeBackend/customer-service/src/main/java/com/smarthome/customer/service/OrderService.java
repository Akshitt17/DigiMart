package com.smarthome.customer.service;

import com.smarthome.customer.client.DeviceServiceClient;
import com.smarthome.customer.client.StockServiceClient;
import com.smarthome.customer.dto.OrderRequest;
import com.smarthome.customer.dto.OrderResponse;
import com.smarthome.customer.entity.*;
import com.smarthome.customer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StockServiceClient stockServiceClient;

    @Autowired
    private DeviceServiceClient deviceServiceClient;

    @Transactional
    public OrderResponse createOrder(OrderRequest request, Long customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        AddressEntity deliveryAddress = addressRepository.findById(request.getDeliveryAddressId())
                .orElseThrow(() -> new RuntimeException("Delivery address not found"));

        // Validate that the address belongs to the customer
        if (!deliveryAddress.getCustomer().getCustomerId().equals(customerId)) {
            throw new RuntimeException("Invalid delivery address for customer");
        }

        // Validate all items and check stock availability
        for (OrderRequest.OrderItemRequest item : request.getOrderItems()) {
            // Verify device exists
            try {
                deviceServiceClient.getDeviceById(item.getDeviceId());
            } catch (Exception e) {
                throw new RuntimeException("Device not found: " + item.getDeviceId());
            }

            // Check stock availability
            try {
                Boolean stockAvailable = stockServiceClient.checkStock(item.getDeviceId(), item.getQuantity()).getBody();
                if (stockAvailable == null || !stockAvailable) {
                    throw new RuntimeException("Insufficient stock for device: " + item.getDeviceId());
                }
            } catch (Exception e) {
                throw new RuntimeException("Stock check failed for device: " + item.getDeviceId());
            }
        }

        // Calculate total amount
        BigDecimal totalAmount = request.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderEntity order = new OrderEntity(customer, deliveryAddress, totalAmount);
        OrderEntity savedOrder = orderRepository.save(order);

        // Create order items and reduce stock
        List<OrderItemEntity> orderItems = request.getOrderItems().stream()
                .map(itemRequest -> {
                    // Reduce stock
                    try {
                        stockServiceClient.reduceStock(itemRequest.getDeviceId(), itemRequest.getQuantity());
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to reduce stock for device: " + itemRequest.getDeviceId());
                    }

                    return new OrderItemEntity(
                            savedOrder,
                            itemRequest.getDeviceId(),
                            itemRequest.getQuantity(),
                            itemRequest.getPrice()
                    );
                })
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);

        return mapToResponse(savedOrder);
    }

    // Gateway ensures customer can only see their orders
    public List<OrderResponse> getCustomerOrders(Long customerId) {
        return orderRepository.findByCustomerCustomerIdOrderByOrderDateDesc(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Gateway ensures proper access control
    public OrderResponse getOrderById(Long orderId, Long customerId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Validate that the order belongs to the customer (for customers)
        if (customerId != null && !order.getCustomer().getCustomerId().equals(customerId)) {
            throw new RuntimeException("Order not found for customer");
        }

        return mapToResponse(order);
    }

    // Gateway ensures only ADMIN can call this
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderEntity.OrderStatus oldStatus = order.getStatus();
        OrderEntity.OrderStatus newStatus = OrderEntity.OrderStatus.valueOf(status.toUpperCase());
        
        order.setStatus(newStatus);
        OrderEntity updatedOrder = orderRepository.save(order);

        // If order is cancelled, restore stock
        if (newStatus == OrderEntity.OrderStatus.CANCELLED && oldStatus != OrderEntity.OrderStatus.CANCELLED) {
            order.getOrderItems().forEach(item -> {
                try {
                    stockServiceClient.restoreStock(item.getDeviceId(), item.getQuantity());
                } catch (Exception e) {
                    System.err.println("Failed to restore stock for device: " + item.getDeviceId());
                }
            });
        }

        return mapToResponse(updatedOrder);
    }

    // Gateway ensures only ADMIN can call this
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private String fetchDeviceName(Long deviceId) {
        try {
                       
            // Call your device service endpoint
            Object deviceResponse = deviceServiceClient.getDeviceById(deviceId).getBody();
            
            if (deviceResponse != null) {
                
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

    private OrderResponse mapToResponse(OrderEntity order) {
    	

        return new OrderResponse(
                order.getOrderId(),
                order.getCustomer().getCustomerId(),
                order.getDeliveryAddress().getAddressId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getOrderItems().stream()
                        .map(item -> new OrderResponse.OrderItemResponse(
                                item.getOrderItemId(),
                                item.getDeviceId(),
                                item.getQuantity(),
                                item.getPrice(),
                                fetchDeviceName(item.getDeviceId())
                        ))
                        .collect(Collectors.toList())
        );
    }
}
