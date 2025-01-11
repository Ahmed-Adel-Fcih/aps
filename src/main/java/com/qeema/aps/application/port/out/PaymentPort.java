package com.qeema.aps.application.port.out;

import com.qeema.aps.domain.payment.Payment;

public interface PaymentPort {
    void processPayment(Payment payment);
}