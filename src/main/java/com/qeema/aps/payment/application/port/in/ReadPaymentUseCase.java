package com.qeema.aps.payment.application.port.in;

import java.util.List;

import com.qeema.aps.payment.domain.Payment;

public interface ReadPaymentUseCase {
    List<Payment> getPaymentList();

    Payment getPayment(Integer id);

    Payment getPaymentByMerchantReference(String merchant_reference);
}
