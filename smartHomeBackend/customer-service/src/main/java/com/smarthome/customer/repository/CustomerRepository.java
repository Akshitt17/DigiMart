package com.smarthome.customer.repository;

import com.smarthome.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByUserId(Long userId);
    Optional<CustomerEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}