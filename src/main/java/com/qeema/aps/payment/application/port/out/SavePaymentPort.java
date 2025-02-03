package com.qeema.aps.payment.application.port.out;

import com.qeema.aps.payment.domain.Payment;
import com.qeema.aps.payment.domain.dto.PreparePaymentRequestDto;

public interface SavePaymentPort {
    void savePayment(Payment payment);

    void saveTokenizationRequest(PreparePaymentRequestDto request, String merchant_reference, Integer customerId);
}