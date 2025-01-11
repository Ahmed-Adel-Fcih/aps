package com.qeema.aps.application.port.in;

import java.util.List;

import com.qeema.aps.domain.payment.Payment;

public interface ProcessPaymentUseCase {
    void processPayment(Payment payment);

    List<Payment> getPaymentList();

    Payment getPayment(Payment payment);
}
