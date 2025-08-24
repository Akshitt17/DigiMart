package com.smarthome.stock.repository;

import com.smarthome.stock.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    
    Optional<StockEntity> findByDeviceId(Long deviceId);
    
    boolean existsByDeviceId(Long deviceId);
    
    @Query("SELECT s FROM StockEntity s WHERE s.quantity > 0")
    List<StockEntity> findInStockItems();
    
    @Query("SELECT s FROM StockEntity s WHERE s.quantity = 0")
    List<StockEntity> findOutOfStockItems();
    
    @Query("SELECT s FROM StockEntity s WHERE s.quantity <= ?1")
    List<StockEntity> findLowStockItems(Integer threshold);
    
    void deleteByDeviceId(Long deviceId);
}
