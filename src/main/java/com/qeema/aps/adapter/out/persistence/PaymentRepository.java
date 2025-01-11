package com.qeema.aps.adapter.out.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.qeema.aps.domain.payment.Payment;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Integer> {
}