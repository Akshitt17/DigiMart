package com.smarthome.customer.repository;

import com.smarthome.customer.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    List<OrderItemEntity> findByOrderOrderId(Long orderId);
    List<OrderItemEntity> findByDeviceId(Long deviceId);
}