package com.smarthome.device.repository;

import com.smarthome.device.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {
    
    List<DeviceEntity> findByDeviceType(String deviceType);
    
    List<DeviceEntity> findByDeviceNameContainingIgnoreCase(String deviceName);
    
    @Query("SELECT d FROM DeviceEntity d WHERE d.price BETWEEN ?1 AND ?2")
    List<DeviceEntity> findByPriceBetween(Double minPrice, Double maxPrice);
    
    List<DeviceEntity> findByCreatedBy(Long createdBy);
}
