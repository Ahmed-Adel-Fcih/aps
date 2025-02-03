package com.qeema.aps.payment.adapter.out.persistance;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.qeema.aps.payment.domain.Payment;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    @Query("SELECT p FROM Payment p WHERE p.merchant_reference = ?1")
    Payment findByMerchantReference(String merchant_reference);
}