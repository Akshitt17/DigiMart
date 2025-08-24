package com.smarthome.customer.repository;

import com.smarthome.customer.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByCustomerCustomerId(Long customerId);
    List<OrderEntity> findByCustomerCustomerIdOrderByOrderDateDesc(Long customerId);
    List<OrderEntity> findByStatus(OrderEntity.OrderStatus status);
    
    @Query("SELECT o FROM OrderEntity o WHERE o.orderDate BETWEEN ?1 AND ?2")
    List<OrderEntity> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
