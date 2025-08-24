package com.smarthome.customer.repository;

import com.smarthome.customer.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findByCustomerCustomerId(Long customerId);
    Optional<AddressEntity> findByCustomerCustomerIdAndIsDefault(Long customerId, Boolean isDefault);
}