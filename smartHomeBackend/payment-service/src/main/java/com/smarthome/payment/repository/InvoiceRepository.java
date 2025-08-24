package com.smarthome.payment.repository;

import com.smarthome.payment.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
    Optional<InvoiceEntity> findByOrderId(Long orderId);
    List<InvoiceEntity> findByCustomerId(Long customerId);
    Optional<InvoiceEntity> findByInvoiceNumber(String invoiceNumber);
}