package com.smarthome.customer.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequest {
    @NotNull(message = "Delivery address ID is required")
    private Long deliveryAddressId;

    @Valid
    private List<OrderItemRequest> orderItems;

    // Constructors, getters and setters
    public OrderRequest() {}

    public Long getDeliveryAddressId() { return deliveryAddressId; }
    public void setDeliveryAddressId(Long deliveryAddressId) { this.deliveryAddressId = deliveryAddressId; }

    public List<OrderItemRequest> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemRequest> orderItems) { this.orderItems = orderItems; }

    public static class OrderItemRequest {
        @NotNull(message = "Device ID is required")
        private Long deviceId;

        @NotNull(message = "Quantity is required")
        private Integer quantity;

        @NotNull(message = "Price is required")
        private BigDecimal price;

        // Constructors, getters and setters
        public OrderItemRequest() {}
        

        public OrderItemRequest(@NotNull(message = "Device ID is required") Long deviceId,
				@NotNull(message = "Quantity is required") Integer quantity,
				@NotNull(message = "Price is required") BigDecimal price) {
			super();
			this.deviceId = deviceId;
			this.quantity = quantity;
			this.price = price;
		}


		public Long getDeviceId() { return deviceId; }
        public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }
}
